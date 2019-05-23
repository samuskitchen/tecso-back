package coop.tecso.examen.model.payload;

import coop.tecso.examen.model.enums.DeviceType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeviceInfo {

    @NotBlank(message = "Device id cannot be blank")
    @ApiModelProperty(value = "Device Id", required = true, dataType = "string", allowableValues = "Non empty string")
    private String deviceId;

    @NotNull(message = "Device type cannot be null")
    @ApiModelProperty(value = "Device type Android/iOS/Web", required = true, dataType = "string", allowableValues =
            "DEVICE_TYPE_ANDROID, DEVICE_TYPE_IOS, DEVICE_TYPE_WEB")
    private DeviceType deviceType;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, DeviceType deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
