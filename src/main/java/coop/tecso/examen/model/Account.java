package coop.tecso.examen.model;

import coop.tecso.examen.model.enums.AccountType;
import coop.tecso.examen.model.enums.Currency;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number_account")
    private String numberAccount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    @Column(name = "type_account")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_person_legal", nullable = false)
    private LegalPerson legalPerson;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_person_physical", nullable = false)
    private PhysicalPerson physicalPerson;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(String numberAccount, Currency currency, BigDecimal balance,
                   AccountType accountType, LegalPerson legalPerson) {
        this.numberAccount = numberAccount;
        this.currency = currency;
        this.balance = balance;
        this.accountType = accountType;
        this.legalPerson = legalPerson;
    }

    public Account(String numberAccount, Currency currency, BigDecimal balance,
                   AccountType accountType, PhysicalPerson physicalPerson) {
        this.numberAccount = numberAccount;
        this.currency = currency;
        this.balance = balance;
        this.accountType = accountType;
        this.physicalPerson = physicalPerson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", numberAccount='" + numberAccount + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", accountType=" + accountType +
                ", legalPerson=" + legalPerson +
                ", physicalPerson=" + physicalPerson +
                '}';
    }
}