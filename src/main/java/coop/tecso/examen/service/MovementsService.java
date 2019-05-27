package coop.tecso.examen.service;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.Movements;
import coop.tecso.examen.model.enums.Currency;
import coop.tecso.examen.model.enums.TypeMovement;
import coop.tecso.examen.repository.AccountRepository;
import coop.tecso.examen.repository.MovementsRepository;
import coop.tecso.examen.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MovementsService {

    public static final String MOVEMENT_REJECTED = "Balance not allowed, movement rejected";

    @Autowired
    private MovementsRepository movementsRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Optional<Movements> saveAllMovements(Movements movements) {
        Optional<Account> optionalAccount = accountRepository.findById(movements.getAccount().getId());
        Optional<Movements> optionalMovement = Optional.ofNullable(movements);

        movements.setAccount(optionalAccount.get());
        if (TypeMovement.CREDITO.equals(movements.getTypeMovement())) {
            return validateMovementCredit(movements, optionalMovement, optionalAccount);
        } else {
            return validateMovementDebit(movements, optionalMovement, optionalAccount);
        }
    }

    public Page<Movements> findMovementByAccount(Long idAccount, int page, int size) {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return movementsRepository.findByAccount(optionalAccount.get(), pageable);
    }

    /**
     * Function that validates the type of movement (CREDIT) and validates the bank account
     *
     * @param movements
     * @param optionalMovement
     * @param optionalAccount
     * @return
     */
    private Optional<Movements> validateMovementCredit(Movements movements, Optional<Movements> optionalMovement, Optional<Account> optionalAccount) {
        return optionalMovement.map(movement -> validateAmount(optionalAccount, movement))
                .filter(aBoolean -> !aBoolean)
                .map(aBoolean -> {
                    Movements movementSave = movementsRepository.save(movements);

                    movements.getAccount().setBalance(movements.getAccount().getBalance().subtract(movements.getAmount()));
                    accountRepository.save(movements.getAccount());
                    return Optional.of(movementSave);
                })
                .orElseThrow(() -> new AppException(MOVEMENT_REJECTED));
    }

    /**
     * Function that validates the type of movement (DEBIT)
     *
     * @param movements
     * @param optionalMovement
     * @param optionalAccount
     * @return
     */
    private Optional<Movements> validateMovementDebit(Movements movements, Optional<Movements> optionalMovement, Optional<Account> optionalAccount) {
        return optionalMovement.map(aBoolean -> {
            Movements movementSave = movementsRepository.save(movements);

            movements.getAccount().setBalance(movements.getAccount().getBalance().add(movements.getAmount()));
            accountRepository.save(movements.getAccount());
            return Optional.of(movementSave);
        })
                .orElseThrow(() -> new AppException(MOVEMENT_REJECTED));
    }

    /**
     * Function that validates the amount of the discovered (financial) is greater than allowed
     *
     * @param optionalAccount
     * @param movements
     * @return
     */
    private boolean validateAmount(Optional<Account> optionalAccount, Movements movements) {
        if (Currency.PESO.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(1000L))) {
            return true;
        } else if (Currency.DOLAR.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(300L))) {
            return true;
        } else return Currency.EURO.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(150L));
    }
}