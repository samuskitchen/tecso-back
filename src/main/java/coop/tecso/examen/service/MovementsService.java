package coop.tecso.examen.service;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.Movements;
import coop.tecso.examen.model.dto.MovementsRequest;
import coop.tecso.examen.model.enums.Currency;
import coop.tecso.examen.model.enums.TypeMovement;
import coop.tecso.examen.repository.AccountRepository;
import coop.tecso.examen.repository.MovementsRepository;
import coop.tecso.examen.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MovementsService {

    public static final String MOVEMENT_REJECTED = "Balance not allowed, movement rejected";
    @Autowired
    private MovementsRepository movementsRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Optional<String> saveAllMovements(MovementsRequest movementsRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(movementsRequest.getIdAccount());
        Optional<List<Movements>> optionalMovements = Optional.ofNullable(movementsRequest.getMovements());

        optionalMovements.map(movements ->
                movements.stream()
                        .filter(movementsFilter -> movementsFilter.getTypeMovement().equals(TypeMovement.CREDITO))
                        .filter(movementsFilter -> validateAmount(optionalAccount, movementsFilter))
                        .map(movement -> {
                            movement.setAccount(optionalAccount.get());
                            Movements movementsReturn = movementsRepository.save(movement);

                            movement.getAccount().setBalance(movement.getAccount().getBalance().subtract(movement.getAmount()));
                            accountRepository.save(movement.getAccount());

                            return movementsReturn;
                        })
        ).orElseThrow(() -> new AppException(MOVEMENT_REJECTED));


        optionalMovements.map(movements ->
                movements.stream()
                        .filter(movementsFilter -> movementsFilter.getTypeMovement().equals(TypeMovement.DEBITO))
                        .map(movement -> {
                            movement.setAccount(optionalAccount.get());
                            Movements movementsReturn = movementsRepository.save(movement);

                            movement.getAccount().setBalance(movement.getAccount().getBalance().subtract(movement.getAmount()));
                            accountRepository.save(movement.getAccount());

                            return movementsReturn;
                        })).orElseThrow(() -> new AppException(MOVEMENT_REJECTED));

        return Optional.of("The movements are added satisfactorily");

        /*Optional.of(movementsRequest.getMovements().stream()
                .filter(movements -> movements.getTypeMovement().equals(TypeMovement.CREDITO))
                .filter(movements -> validateAmount(optionalAccount, movements))
                .map(movements -> {
                    movements.setAccount(optionalAccount.get());
                    Movements movementsReturn = movementsRepository.save(movements);

                    movements.getAccount().setBalance(movements.getAccount().getBalance().subtract(movements.getAmount()));
                    accountRepository.save(movements.getAccount());

                    return movementsReturn;
                })).orElseThrow(() -> new AppException(MOVEMENT_REJECTED));

        Optional.of(movementsRequest.getMovements().stream()
                .filter(movements -> movements.getTypeMovement().equals(TypeMovement.DEBITO))
                .map(movements -> {
                    movements.setAccount(optionalAccount.get());
                    Movements movementsReturn = movementsRepository.save(movements);

                    movements.getAccount().setBalance(movements.getAccount().getBalance().add(movements.getAmount()));
                    accountRepository.save(movements.getAccount());

                    return movementsReturn;
                })).orElseThrow(() -> new AppException(MOVEMENT_REJECTED));*/
    }

    private boolean validateAmount(Optional<Account> optionalAccount, Movements movements) {
        if(Currency.PESO.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(1000L))){
            return true;
        }else if(Currency.DOLAR.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(300L))){
            return true;
        }else if(Currency.EURO.equals(optionalAccount.get().getCurrency())
                && Util.validateAmount(movements.getAmount(), BigDecimal.valueOf(150L))){
            return true;
        }
        return false;
    }
}