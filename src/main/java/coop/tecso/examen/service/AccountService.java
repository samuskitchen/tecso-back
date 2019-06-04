package coop.tecso.examen.service;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.LegalPerson;
import coop.tecso.examen.model.PhysicalPerson;
import coop.tecso.examen.model.enums.AccountType;
import coop.tecso.examen.repository.AccountRepository;
import coop.tecso.examen.repository.LegalPersonRepository;
import coop.tecso.examen.repository.MovementsRepository;
import coop.tecso.examen.repository.PhysicalPersonRepository;
import coop.tecso.examen.util.Util;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"accounts"})
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

    @Autowired
    private MovementsRepository movementsRepository;


    public Optional<Account> saveAccount(Account account) {
        Optional<LegalPerson> legalPersonOptional = Optional.ofNullable(account.getLegalPerson());
        Optional<PhysicalPerson> physicalPersonOptional = Optional.ofNullable(account.getPhysicalPerson());

        if (ObjectUtils.allNotNull(account.getLegalPerson()) && !Objects.isNull(account.getLegalPerson())) {
            return validateLegalPerson(account, legalPersonOptional);
        } else {
            return validatePhysicalPerson(account, physicalPersonOptional);
        }
    }

    public Optional<String> updateAccount(Account account) {
        Optional<Account> optionalAccount = Optional.ofNullable(account);

        return optionalAccount.map(ac -> {
            accountRepository.executeUpdateAccount(ac.getId(),
                    ac.getCurrency().name(), ac.getBalance());
            return Optional.of("Account Update Successfully");
        }).orElseThrow(() -> new AppException("Error Update Account"));
    }

    public Optional<String> deleteAccountById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        return optionalAccount.filter(account -> !movementsRepository.existsByAccount(account))
                .map(account -> {

                    if (!Objects.isNull(account.getLegalPerson())) {
                        legalPersonRepository.delete(account.getLegalPerson());
                    } else {
                        physicalPersonRepository.delete(account.getPhysicalPerson());
                    }

                    accountRepository.delete(account);
                    return Optional.of("The account was successfully deleted");
                }).orElseThrow(() -> new AppException("The account can only be deleted if they do not have associated movements"));
    }

    public Page<Account> findAccountByType(AccountType accountType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return accountRepository.findAccountByAccountType(accountType, pageable);
    }

    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
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
                    account.setLegalPerson(null);
                    account.setNumberAccount(getNumberAccount());
                    account.getPhysicalPerson().setActive(true);
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
                    account.setPhysicalPerson(null);
                    account.setNumberAccount(getNumberAccount());
                    account.getLegalPerson().setActive(true);
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
        } else return existRutPhysical;
    }

}