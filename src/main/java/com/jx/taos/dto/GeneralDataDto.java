package com.jx.taos.dto;

import com.jx.message.device2platform.AlarmItem;
import com.jx.message.device2platform.GeneralInfo;

import java.util.List;

/**
 * 综合信息上报的dto
 *
 * @author yusijia 2020-05-12 16:48
 */
public class GeneralDataDto {
    /**
     * 补传标识。0：正常，1：补传
     */
    private Byte reReportFlag = 0;
    private Short messageSequence;
    private Short messageType;
    private String productKey;
    private String deviceCode;
    private List<AlarmItem> alarmList;

    /**
     * 定位状态。0：未定位；1：定位。
     */
    private Byte locationState;

    /**
     * 南北纬状态。0：北纬；1：南纬
     */
    private Byte latitudeState;

    /**
     * 东西经状态。0：东经；1：西经
     */
    private Byte longitudeState;

    private Double latitude;
    private Double longitude;
    private Short altitude;
    private Short speed;
    private Short direction;

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
     * 最低三位高到低分别为离合器、卸液泵、紧急阀，1开 0 关
     */
    private Byte valveInfo;

    /**
     * 最低7位从高到低分别为：
     * 操作箱开关、
     * 限高开关、
     * 气态冲液枪开关、
     * 液态冲液枪开关、
     * 车载紧急停止开关、
     * 遥控器一键卸液开关、
     * 遥控器紧急停止开关
     * 1 开  0 关
     */
    private Byte switchInfo;

    /**
     * 流量计。以 kg 为单位乘以 100，精确到 0.01kg。
     */
    private Double flowmeter;

    /**
     * 采集时间
     */
    private String collectTime;


    public GeneralDataDto(){}

    public GeneralDataDto(Short messageSequence, Short messageType, String productKey, String deviceCode, GeneralInfo generalInfo){
        this.messageSequence = messageSequence;
        this.messageType = messageType;
        this.productKey = productKey;
        this.deviceCode = deviceCode;

        this.flowmeter = generalInfo.getFlowmeter() != null ? generalInfo.getFlowmeter()/100D : 0;
        this.valveInfo = generalInfo.getValveInfo();
        this.switchInfo = generalInfo.getSwitchInfo();

        if(generalInfo.getValveInfo() != null){
            this.temperature = generalInfo.getTankInfo().getTemperature()/10D;
            this.pressure = generalInfo.getTankInfo().getPressure()/100D;
            this.liquidLevel = generalInfo.getTankInfo().getLiquidLevel();
            this.gasConcentration = generalInfo.getTankInfo().getGasConcentration()/100D;
        }

        if(generalInfo.getLocationInfo() != null){
            this.locationState = generalInfo.getLocationInfo().getLocationState();
            this.latitudeState = generalInfo.getLocationInfo().getLatitudeState();
            this.longitudeState = generalInfo.getLocationInfo().getLongitudeState();
            this.longitude = generalInfo.getLocationInfo().getLongitude()/1000000D;
            this.latitude = generalInfo.getLocationInfo().getLatitude()/1000000D;
            this.altitude = generalInfo.getLocationInfo().getAltitude();
            this.direction = generalInfo.getLocationInfo().getDirection();
            this.speed = (short)(generalInfo.getLocationInfo().getSpeed()/10);
        }

        this.alarmList = generalInfo.getAlarmList();
        this.collectTime = generalInfo.getCollectTime();
    }

    public GeneralDataDto(Byte reReportFlag, Short messageSequence, Short messageType, String productKey, String deviceCode, GeneralInfo generalInfo){
        this(messageSequence, messageType, productKey, deviceCode, generalInfo);
        this.reReportFlag = reReportFlag;
    }

    public Byte getReReportFlag() {
        return reReportFlag;
    }

    public void setReReportFlag(Byte reReportFlag) {
        this.reReportFlag = reReportFlag;
    }

    public Short getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(Short messageSequence) {
        this.messageSequence = messageSequence;
    }

    public Short getMessageType() {
        return messageType;
    }

    public void setMessageType(Short messageType) {
        this.messageType = messageType;
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

    public Byte getLocationState() {
        return locationState;
    }

    public void setLocationState(Byte locationState) {
        this.locationState = locationState;
    }

    public Byte getLatitudeState() {
        return latitudeState;
    }

    public void setLatitudeState(Byte latitudeState) {
        this.latitudeState = latitudeState;
    }

    public Byte getLongitudeState() {
        return longitudeState;
    }

    public void setLongitudeState(Byte longitudeState) {
        this.longitudeState = longitudeState;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Short getAltitude() {
        return altitude;
    }

    public void setAltitude(Short altitude) {
        this.altitude = altitude;
    }

    public Short getSpeed() {
        return speed;
    }

    public void setSpeed(Short speed) {
        this.speed = speed;
    }

    public Short getDirection() {
        return direction;
    }

    public void setDirection(Short direction) {
        this.direction = direction;
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

    public Short getLiquidLevel() {
        return liquidLevel;
    }

    public void setLiquidLevel(Short liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public Double getGasConcentration() {
        return gasConcentration;
    }

    public void setGasConcentration(Double gasConcentration) {
        this.gasConcentration = gasConcentration;
    }

    public Byte getValveInfo() {
        return valveInfo;
    }

    public void setValveInfo(Byte valveInfo) {
        this.valveInfo = valveInfo;
    }

    public Byte getSwitchInfo() {
        return switchInfo;
    }

    public void setSwitchInfo(Byte switchInfo) {
        this.switchInfo = switchInfo;
    }

    public Double getFlowmeter() {
        return flowmeter;
    }

    public void setFlowmeter(Double flowmeter) {
        this.flowmeter = flowmeter;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }
}
