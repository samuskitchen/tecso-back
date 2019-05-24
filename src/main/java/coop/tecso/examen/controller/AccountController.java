package coop.tecso.examen.controller;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private AccountService accountService;


    @PostMapping(path = "save")
    public ResponseEntity<Account> accountSave(@Valid @RequestBody Account account) {
        return accountService.saveAccount(account)
                .map(accountSave -> {
                    logger.info("Created new Account: " + account);
                    return ResponseEntity.ok(accountSave);
                })
                .orElseThrow(() -> new AppException("Unexpected error during save account. Please logout and login again."));
    }
}
