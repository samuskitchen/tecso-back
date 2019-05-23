package coop.tecso.examen.model;

import coop.tecso.examen.model.audit.DateAudit;
import coop.tecso.examen.model.enums.DeviceType;
import coop.tecso.examen.model.token.RefreshToken;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_device")
public class UserDevice extends DateAudit {

    @Id
    @Column(name = "user_device_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_type")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "is_refresh_active")
    private Boolean isRefreshActive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @OneToOne(optional = false, mappedBy = "userDevice")
    private RefreshToken refreshToken;

    public UserDevice() {
    }

    public UserDevice(Long id, User user, DeviceType deviceType, String deviceId,
                      RefreshToken refreshToken, Boolean isRefreshActive) {
        this.id = id;
        this.user = user;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.refreshToken = refreshToken;
        this.isRefreshActive = isRefreshActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Boolean getRefreshActive() {
        return isRefreshActive;
    }

    public void setRefreshActive(Boolean refreshActive) {
        isRefreshActive = refreshActive;
    }
}
