package coop.tecso.examen.repository;

import coop.tecso.examen.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

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
}
