package coop.tecso.examen.repository;

import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.enums.AccountType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @CacheEvict(value = "accountById", key = "#p0.longValue()")
    @Query(value =
            "SELECT " +
                    "id, " +
                    "number_account, " +
                    "currency, " +
                    "balance, " +
                    "type_account, " +
                    "fk_person_legal, " +
                    "fk_person_physical " +
                    "FROM account " +
                    "WHERE id = ?1", nativeQuery = true)
    Optional<Account> findById(Long id);

    @CacheEvict(value = "accountByAccountType", key = "#p0")
    Page<Account> findAccountByAccountType(AccountType accountType, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE account " +
                "SET currency = :currency, " +
                "balance = :balance " +
            "WHERE id = :id ", nativeQuery = true)
    void executeUpdateAccount(@Param(value = "id") Long id,
                       @Param(value = "currency") String currency,
                       @Param(value = "balance") BigDecimal balance);

}
