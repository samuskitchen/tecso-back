package coop.tecso.examen.controller;

import coop.tecso.examen.annotation.CurrentUser;
import coop.tecso.examen.model.CustomUserDetails;
import coop.tecso.examen.model.payload.ApiResponse;
import coop.tecso.examen.model.payload.LogOutRequest;
import coop.tecso.examen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Api(value = "User Rest API", description = "Defines endpoints for the logged in user. It's secured by default")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logoutUser(@CurrentUser CustomUserDetails customUserDetails,
                                     @ApiParam(value = "The LogOutRequest payload") @Valid @RequestBody LogOutRequest logOutRequest) {
        userService.logoutUser(customUserDetails, logOutRequest);
        return ResponseEntity.ok(new ApiResponse("Log out successful", true));
    }

}