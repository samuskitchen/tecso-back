package coop.tecso.examen.model.token;

import coop.tecso.examen.model.UserDevice;
import coop.tecso.examen.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;

@Entity(name = "refresh_token")
public class RefreshToken extends DateAudit {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

    @Column(name = "refresh_count")
    private Long refreshCount;

    @Column(name = "expiry_dt", nullable = false)
    private Instant expiryDate;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_device_id", unique = true)
    private UserDevice userDevice;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, UserDevice userDevice, Long refreshCount, Instant expiryDate) {
        this.id = id;
        this.token = token;
        this.userDevice = userDevice;
        this.refreshCount = refreshCount;
        this.expiryDate = expiryDate;
    }

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(Long refreshCount) {
        this.refreshCount = refreshCount;
    }
}
