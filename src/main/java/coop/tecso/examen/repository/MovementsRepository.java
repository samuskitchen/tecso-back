package coop.tecso.examen.repository;

import coop.tecso.examen.model.Account;
import coop.tecso.examen.model.Movements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementsRepository extends JpaRepository<Movements, Long> {

    @Cacheable("findByAccount")
    Page<Movements> findByAccount(Account account, Pageable pageable);

    boolean existsByAccount(Account account);

}
