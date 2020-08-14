package com.jx.taos.dto;

/**
 * 设备校验信息
 *
 * @author yusijia 2020-05-07 10:33
 */
public class EquipmentAuthInfo {
    private String productKey;
    private String productSecret;
    private String deviceCode;
    private String deviceSecret;



    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }
}
