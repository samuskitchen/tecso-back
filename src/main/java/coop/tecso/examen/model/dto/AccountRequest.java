package coop.tecso.examen.model.dto;

import coop.tecso.examen.model.LegalPerson;
import coop.tecso.examen.model.PhysicalPerson;

public class AccountRequest {

    private Long idAccount;

    private Long idLegalPerson;

    private Long idPhysicalPerson;

    private Boolean activeHolder;

    private LegalPerson legalPerson;

    private PhysicalPerson physicalPerson;

    public AccountRequest() {
    }

    public AccountRequest(Long idAccount, Long idLegalPerson, Long idPhysicalPerson, Boolean activeHolder, LegalPerson legalPerson, PhysicalPerson physicalPerson) {
        this.idAccount = idAccount;
        this.idLegalPerson = idLegalPerson;
        this.idPhysicalPerson = idPhysicalPerson;
        this.activeHolder = activeHolder;
        this.legalPerson = legalPerson;
        this.physicalPerson = physicalPerson;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Long getIdLegalPerson() {
        return idLegalPerson;
    }

    public void setIdLegalPerson(Long idLegalPerson) {
        this.idLegalPerson = idLegalPerson;
    }

    public Long getIdPhysicalPerson() {
        return idPhysicalPerson;
    }

    public void setIdPhysicalPerson(Long idPhysicalPerson) {
        this.idPhysicalPerson = idPhysicalPerson;
    }

    public Boolean getActiveHolder() {
        return activeHolder;
    }

    public void setActiveHolder(Boolean activeHolder) {
        this.activeHolder = activeHolder;
    }

    public LegalPerson getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(LegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }

    public PhysicalPerson getPhysicalPerson() {
        return physicalPerson;
    }

    public void setPhysicalPerson(PhysicalPerson physicalPerson) {
        this.physicalPerson = physicalPerson;
    }
}
