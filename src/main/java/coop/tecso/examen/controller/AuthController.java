package coop.tecso.examen.controller;

import coop.tecso.examen.event.OnUserRegistrationCompleteEvent;
import coop.tecso.examen.exception.InvalidTokenRequestException;
import coop.tecso.examen.exception.TokenRefreshException;
import coop.tecso.examen.exception.UserLoginException;
import coop.tecso.examen.exception.UserRegistrationException;
import coop.tecso.examen.model.CustomUserDetails;
import coop.tecso.examen.model.payload.ApiResponse;
import coop.tecso.examen.model.payload.JwtAuthenticationResponse;
import coop.tecso.examen.model.payload.LoginRequest;
import coop.tecso.examen.model.payload.RegistrationRequest;
import coop.tecso.examen.model.payload.TokenRefreshRequest;
import coop.tecso.examen.model.token.RefreshToken;
import coop.tecso.examen.security.JwtTokenProvider;
import coop.tecso.examen.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(value = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")

public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider tokenProvider, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Checks is a given email is in use or not.
     */
    @ApiOperation(value = "Checks if the given email is in use")
    @GetMapping("/checkEmailInUse")
    public ResponseEntity checkEmailInUse(@ApiParam(value = "Email id to check against") @RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
    }

    /**
     * Checks is a given username is in use or not.
     */
    @ApiOperation(value = "Checks if the given username is in use")
    @GetMapping("/checkUsernameInUse")
    public ResponseEntity checkUsernameInUse(@ApiParam(value = "Username to check against") @RequestParam(
            "username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
    }


    /**
     * Entry point for the user log in. Return the jwt auth token and the refresh token
     */
    @PostMapping("/login")
    @ApiOperation(value = "Logs the user in to the system and return the auth tokens")
    public ResponseEntity authenticateUser(@ApiParam(value = "The LoginRequest payload") @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(customUserDetails);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }

    /**
     * Entry point for the user registration process. On successful registration,
     * publish an event to generate email verification token
     */
    @PostMapping("/register")
    @ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
    public ResponseEntity registerUser(@ApiParam(value = "The RegistrationRequest payload") @Valid @RequestBody RegistrationRequest registrationRequest) {

        return authService.registerUser(registrationRequest)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("Registered User returned [API[: " + user);
                    return ResponseEntity.ok(new ApiResponse("User registered successfully. Check your email for verification", true));
                })
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "Missing user object in database"));
    }

    /**
     * Confirm the email verification token generated for the user during
     * registration. If token is invalid or token is expired, report error.
     */
    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
    public ResponseEntity confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {

        return authService.confirmEmailRegistration(token)
                .map(user -> ResponseEntity.ok(new ApiResponse("User verified successfully", true)))
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token, "Failed to confirm. Please generate a new email verification request"));
    }

    /**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     */
    @PostMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
            "updated response tokens")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));
    }
}
