package com.jx.taos.dto;

import com.jx.message.device2platform.AlarmItem;
import com.jx.message.device2platform.TankReportData;

import java.util.List;

/**
 * 储罐上报数据dto
 *
 * @author yusijia 2020-05-12 15:19
 */
public class TankDataDto {
    private Short messageSequence;
    private String productKey;
    private String deviceCode;

    private List<AlarmItem> alarmList;

    /**
     * 温度。以摄氏度为单位乘以 10，精确到 0.1 摄氏度
     */
    private Double temperature;

    /**
     * 压力。以 Mpa 为单位的压力值乘以 10 的 2 次方，精确到百分之一 Mpa
     */
    private Double pressure;

    /**
     * 液位。单位 mm。
     */
    private Short liquidLevel;

    /**
     * 气体浓度。以百分比为单位的浓度值乘以 100，精确到小数点后 2 位。
     */
    private Double gasConcentration;

    /**
     * 储罐的供气压力。
     * 以 Mpa 为单位的压力值乘以 10 的 2 次方，精确到百分之一 Mpa。
     */
    private Double supplyPressure;

    /**
     * 储罐的开关信息
     * 最低两位高到低分别为气相阀开关，液相阀开关。1开，0关。
     */
    private Byte switchInfo;

    /**
     * 储罐的信号强度，0-31
     */
    private Short rssi;


    /**
     * 采集时间
     */
    private String collectTime;


    public TankDataDto(){}

    public TankDataDto(Short messageSequence, String productKey, String deviceCode, TankReportData reportData){
        this.messageSequence = messageSequence;
        this.productKey = productKey;
        this.deviceCode = deviceCode;
        this.temperature = reportData.getTankInfo().getTemperature()/10D;
        this.pressure = reportData.getTankInfo().getPressure()/100D;
        this.liquidLevel = reportData.getTankInfo().getLiquidLevel();
        this.gasConcentration = reportData.getTankInfo().getGasConcentration()/100D;

        this.supplyPressure = reportData.getSupplyPressure()/100D;
        this.switchInfo = reportData.getSwitchInfo();
        this.rssi = reportData.getRssi();

        this.alarmList = reportData.getAlarmList();
        this.collectTime = reportData.getCollectTime();
    }

    public Short getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(Short messageSequence) {
        this.messageSequence = messageSequence;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public List<AlarmItem> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<AlarmItem> alarmList) {
        this.alarmList = alarmList;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getGasConcentration() {
        return gasConcentration;
    }

    public void setGasConcentration(Double gasConcentration) {
        this.gasConcentration = gasConcentration;
    }

    public Short getLiquidLevel() {
        return liquidLevel;
    }

    public void setLiquidLevel(Short liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public Double getSupplyPressure() {
        return supplyPressure;
    }

    public void setSupplyPressure(Double supplyPressure) {
        this.supplyPressure = supplyPressure;
    }

    public Byte getSwitchInfo() {
        return switchInfo;
    }

    public void setSwitchInfo(Byte switchInfo) {
        this.switchInfo = switchInfo;
    }

    public Short getRssi() {
        return rssi;
    }

    public void setRssi(Short rssi) {
        this.rssi = rssi;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }
}
