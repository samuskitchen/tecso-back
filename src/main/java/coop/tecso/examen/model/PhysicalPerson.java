package coop.tecso.examen.model;

import coop.tecso.examen.model.enums.DocumentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "physical_person")
public class PhysicalPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "number_document")
    private String numberDocument;

    @Column(name = "rut", unique = true)
    private String rut;

    private Boolean active;

    public PhysicalPerson() {
    }

    public PhysicalPerson(String name, String surname, DocumentType documentType,
                          String numberDocument, String rut, Boolean active) {
        this.name = name;
        this.surname = surname;
        this.documentType = documentType;
        this.numberDocument = numberDocument;
        this.rut = rut;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}