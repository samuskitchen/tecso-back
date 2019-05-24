package coop.tecso.examen.repository;

import coop.tecso.examen.model.LegalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalPersonRepository extends JpaRepository<LegalPerson, Long> {

    Boolean existsByRut(String rut);
}
