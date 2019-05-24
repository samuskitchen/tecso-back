package coop.tecso.examen.model;

import coop.tecso.examen.model.enums.TypeMovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "movements")
public class Movements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC);

    @Column(name = "type_movement")
    @Enumerated(EnumType.STRING)
    private TypeMovement typeMovement;

    private String description;

    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "fk_id_account", referencedColumnName = "id")
    private LegalPerson legalPerson;

    public Movements() {
    }

    public Movements(OffsetDateTime date, TypeMovement typeMovement, String description, BigDecimal amount, LegalPerson legalPerson) {
        this.date = date;
        this.typeMovement = typeMovement;
        this.description = description;
        this.amount = amount;
        this.legalPerson = legalPerson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public TypeMovement getTypeMovement() {
        return typeMovement;
    }

    public void setTypeMovement(TypeMovement typeMovement) {
        this.typeMovement = typeMovement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LegalPerson getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(LegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }
}
