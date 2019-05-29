package coop.tecso.examen.controller;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.dto.AccountRequest;
import coop.tecso.examen.model.enums.AccountType;
import coop.tecso.examen.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;


    @PostMapping(path = "save")
    public ResponseEntity<Account> accountSave(@RequestBody Account account) {
        return accountService.saveAccount(account)
                .map(accountSave -> {
                    logger.info("Created new Account: " + accountSave.toString());
                    return ResponseEntity.ok(accountSave);
                })
                .orElseThrow(() -> new AppException("Unexpected error during save account. Please try again"));
    }

    @PutMapping(path = "update")
    public ResponseEntity<Account> accountUpdate(@RequestBody AccountRequest accountRequest) {
        return accountService.updateAccount(accountRequest)
                .map(account -> {
                    logger.info("Update Account: " + account.toString());
                    return ResponseEntity.ok(account);
                })
                .orElseThrow(() -> new AppException("Unexpected error during update account. Please try again"));
    }

    @DeleteMapping(path = "delete")
    public ResponseEntity<String> deleteAccountBy(@RequestParam Long id) {
        return accountService.deleteAccountById(id)
                .map(account -> {
                    logger.info("Delete Account");
                    return ResponseEntity.ok(account);
                }).orElseThrow(() -> new AppException("Unexpected error during delete account. Please try again"));
    }

    @GetMapping(path = "find/type")
    public Page<Account> accountFindByType(@RequestParam AccountType accountType,
                                           @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return accountService.findAccountByType(accountType, page, size);
    }

    @GetMapping(path = "find/id")
    public ResponseEntity<Account> findAccountById(@RequestParam(value = "id") Long id){
        return accountService.findAccountById(id)
                .map(account -> {
                    logger.info("Find Account By Id [" + id + "]");
                    return ResponseEntity.ok(account);
                }).orElseThrow(() -> new AppException("Unexpected error during find account by id. Please try again"));
    }
}
