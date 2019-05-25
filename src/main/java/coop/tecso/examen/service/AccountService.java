package coop.tecso.examen.service;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.LegalPerson;
import coop.tecso.examen.model.PhysicalPerson;
import coop.tecso.examen.model.dto.AccountRequest;
import coop.tecso.examen.repository.AccountRepository;
import coop.tecso.examen.repository.LegalPersonRepository;
import coop.tecso.examen.repository.PhysicalPersonRepository;
import coop.tecso.examen.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    public static final String MESSAGES_VERIFY_RUT = "This RUT is already registered, please verify the information";
    public static final String ACCOUNT = "account";
    public static final String EXIST = "exist";
    public static final String ERROR_UPDATING_ACCOUNT = "Error Updating The Account";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LegalPersonRepository legalPersonRepository;

    @Autowired
    private PhysicalPersonRepository physicalPersonRepository;


    public Optional<Account> saveAccount(Account account) {
        Optional<LegalPerson> legalPersonOptional = Optional.ofNullable(account.getLegalPerson());
        Optional<PhysicalPerson> physicalPersonOptional = Optional.ofNullable(account.getPhysicalPerson());

        if (legalPersonOptional.isPresent()) {
            return validateLegalPerson(account, legalPersonOptional);
        } else {
            return validatePhysicalPerson(account, physicalPersonOptional);
        }
    }

    public Optional<Account> updateAccount(AccountRequest accountRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(accountRequest.getIdAccount());
        Optional<LegalPerson> legalPersonOptional = Optional.ofNullable(accountRequest.getLegalPerson());
        Optional<PhysicalPerson> physicalPersonOptional = Optional.ofNullable(accountRequest.getPhysicalPerson());

        if (null != accountRequest.getIdLegalPerson()) {
            return updateAccountLegal(accountRequest, optionalAccount, legalPersonOptional);
        } else {
            return updateAccountPhysical(accountRequest, optionalAccount, physicalPersonOptional);
        }

    }

    /**
     * Intermediate function that validates the type of execution for legal person
     *
     * @param accountRequest
     * @param optionalAccount
     * @param legalPersonOptional
     * @return
     */
    private Optional<Account> updateAccountLegal(AccountRequest accountRequest, Optional<Account> optionalAccount, Optional<LegalPerson> legalPersonOptional) {

        if (legalPersonOptional.isPresent() && !accountRequest.getActiveHolder()) {
            return updateAccountDataLegal(accountRequest, optionalAccount);
        } else {
            return updateAccountNewLegal(accountRequest, optionalAccount);
        }

    }

    /**
     * Intermediate function that validates the execution type for physical person
     *
     * @param accountRequest
     * @param optionalAccount
     * @param physicalPersonOptional
     * @return
     */
    private Optional<Account> updateAccountPhysical(AccountRequest accountRequest, Optional<Account> optionalAccount, Optional<PhysicalPerson> physicalPersonOptional) {

        if (physicalPersonOptional.isPresent() && !accountRequest.getActiveHolder()) {
            return updateAccountDataPhysical(accountRequest, optionalAccount);
        } else {
            return updateAccountNewPhysical(accountRequest, optionalAccount);
        }
    }

    /**
     * Function to update the data of an individual
     *
     * @param accountRequest
     * @param optionalAccount
     * @return
     */
    private Optional<Account> updateAccountDataPhysical(AccountRequest accountRequest, Optional<Account> optionalAccount) {
        return optionalAccount
                .map(account -> {
                    account.getPhysicalPerson().setName(accountRequest.getPhysicalPerson().getName());
                    account.getPhysicalPerson().setSurname(accountRequest.getPhysicalPerson().getSurname());
                    account.getPhysicalPerson().setDocumentType(accountRequest.getPhysicalPerson().getDocumentType());

                    return Optional.of(accountRepository.save(account));
                }).orElseThrow(() -> new AppException(ERROR_UPDATING_ACCOUNT));
    }

    /**
     * Function to update the data of a legal person
     *
     * @param accountRequest
     * @param optionalAccount
     * @return
     */
    private Optional<Account> updateAccountDataLegal(AccountRequest accountRequest, Optional<Account> optionalAccount) {
        return optionalAccount
                .map(account -> {
                    account.getLegalPerson().setBusinessName(accountRequest.getLegalPerson().getBusinessName());
                    account.getLegalPerson().setFoundationYear(accountRequest.getLegalPerson().getFoundationYear());

                    return Optional.of(accountRepository.save(account));
                }).orElseThrow(() -> new AppException(ERROR_UPDATING_ACCOUNT));
    }

    /**
     * Function to remove the current physical person and associate a new natural person
     *
     * @param accountRequest
     * @param optionalAccount
     * @return
     */
    private Optional<Account> updateAccountNewPhysical(AccountRequest accountRequest, Optional<Account> optionalAccount) {
        return optionalAccount
                .map(account -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(ACCOUNT, account);
                    objectMap.put(EXIST, validateExistsRut(accountRequest.getPhysicalPerson().getRut()));

                    return objectMap;
                }).filter(objectMap -> !(Boolean) objectMap.get(EXIST))
                .map(objectMap -> {
                    Account account = (Account) objectMap.get(ACCOUNT);
                    account.getPhysicalPerson().setActive(false);
                    accountRepository.save(account);

                    account.setPhysicalPerson(accountRequest.getPhysicalPerson());
                    return Optional.of(accountRepository.save(account));
                }).orElseThrow(() -> new AppException(MESSAGES_VERIFY_RUT));
    }

    /**
     * Function to remove the current legal person and associate a new legal person
     *
     * @param accountRequest
     * @param optionalAccount
     * @return
     */
    private Optional<Account> updateAccountNewLegal(AccountRequest accountRequest, Optional<Account> optionalAccount) {
        return optionalAccount
                .map(account -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(ACCOUNT, account);
                    objectMap.put(EXIST, validateExistsRut(accountRequest.getLegalPerson().getRut()));

                    return objectMap;
                }).filter(objectMap -> !(Boolean) objectMap.get(EXIST))
                .map(objectMap -> {
                    Account account = (Account) objectMap.get(ACCOUNT);
                    account.getLegalPerson().setActive(false);
                    accountRepository.save(account);

                    account.setLegalPerson(accountRequest.getLegalPerson());
                    return Optional.of(accountRepository.save(account));
                }).orElseThrow(() -> new AppException(MESSAGES_VERIFY_RUT));
    }

    /**
     * Function for if the person is physical and save account
     *
     * @param account
     * @param physicalPersonOptional
     * @return
     */
    private Optional<Account> validatePhysicalPerson(Account account, Optional<PhysicalPerson> physicalPersonOptional) {
        return physicalPersonOptional.map(physicalPerson -> validateExistsRut(physicalPerson.getRut()))
                .filter(aBoolean -> !aBoolean)
                .map(aBoolean -> {
                    account.setNumberAccount(getNumberAccount());
                    Account ac = new Account(account.getNumberAccount(),
                            account.getCurrency(),
                            account.getBalance(),
                            account.getAccountType(),
                            account.getPhysicalPerson()
                    );
                    return Optional.of(accountRepository.save(ac));
                })
                .orElseThrow(() -> new AppException(MESSAGES_VERIFY_RUT));
    }

    /**
     * Function for if the person is legal and save account
     *
     * @param account
     * @param legalPersonOptional
     */
    private Optional<Account> validateLegalPerson(Account account, Optional<LegalPerson> legalPersonOptional) {
        return legalPersonOptional.map(legalPerson -> validateExistsRut(legalPerson.getRut()))
                .filter(aBoolean -> !aBoolean)
                .map(aBoolean -> {
                    account.setNumberAccount(getNumberAccount());
                    Account ac = new Account(
                            account.getNumberAccount(),
                            account.getCurrency(),
                            account.getBalance(),
                            account.getAccountType(),
                            account.getLegalPerson()
                    );
                    return Optional.of(accountRepository.save(ac));
                })
                .orElseThrow(() -> new AppException(MESSAGES_VERIFY_RUT));
    }

    /**
     * Function that generates the bank account number
     *
     * @return
     */
    private String getNumberAccount() {
        return Util.generateRandomAccount(11, false, true);
    }


    /**
     * Function to validate if there is a registered rut in a different holder (Legal or Physical)
     *
     * @param rut
     * @return
     */
    private Boolean validateExistsRut(String rut) {

        Boolean existRutLegal = legalPersonRepository.existsByRut(rut);
        Boolean existRutPhysical = physicalPersonRepository.existsByRut(rut);

        if (existRutLegal) {
            return true;
        } else if (existRutPhysical) {
            return true;
        } else {
            return false;
        }
    }

}