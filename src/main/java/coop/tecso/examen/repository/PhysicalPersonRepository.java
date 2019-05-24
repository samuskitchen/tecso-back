package coop.tecso.examen.repository;

import coop.tecso.examen.model.PhysicalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalPersonRepository extends JpaRepository<PhysicalPerson, Long> {

    Boolean existsByRut(String rut);
}
