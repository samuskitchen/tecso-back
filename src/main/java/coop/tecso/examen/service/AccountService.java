package coop.tecso.examen.service;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.LegalPerson;
import coop.tecso.examen.model.PhysicalPerson;
import coop.tecso.examen.repository.AccountRepository;
import coop.tecso.examen.repository.LegalPersonRepository;
import coop.tecso.examen.repository.PhysicalPersonRepository;
import coop.tecso.examen.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    public static final String MESSAGES_VERIFY_RUT = "This RUT is already registered, please verify the information";

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

    /*public Optional<Account> updateAccount(Account account) {
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());

        optionalAccount.map(ac -> {
            if(account.getLegalPerson().getId().equals(ac.getLegalPerson().getId())){
                legalPersonRepository.save(account.getLegalPerson());
            }else {

            }
        })



        return Optional.of(null);
    }*/

    /**
     * Function for if the person is physical and save account
     *
     * @param account
     * @param physicalPersonOptional
     * @return
     */
    private Optional<Account> validatePhysicalPerson(Account account, Optional<PhysicalPerson> physicalPersonOptional) {
        return physicalPersonOptional.map(physicalPerson -> validateExistsRut(physicalPerson.getRut()))
                .filter(aBoolean -> aBoolean == false)
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
                .filter(aBoolean -> aBoolean == false)
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
