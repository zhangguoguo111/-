package com.jx.taos.service.Impl;

import com.alibaba.fastjson.JSON;
import com.jx.message.constans.MessageType;
import com.jx.taos.service.CreateTableService;
import com.jx.taos.utils.TaosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author lixingao
 * @date 2020/7/16 11:20
 */
@Service
public class CreateTableServiceImpl implements CreateTableService {
    private static Logger log = LoggerFactory.getLogger(CreateTableServiceImpl.class);

    @Override
    public void createTable(String productKey, String deviceCode, Short messageType) {
        MessageType messageTypeEnum = MessageType.getEnumByMessageType(messageType);
        switch (messageTypeEnum) {
            case general_info:
            case batch_general_info:
                createGpsTable(productKey, deviceCode);
                break;
            case tank_info:
                createTankTable(productKey, deviceCode);
                break;
            default:
                log.debug("Can't create table", productKey + deviceCode);

        }
    }

    public static void createGpsTable(String productKey,String deviceCode){
        String str = "CREATE TABLE if not exists t_gps_data (\n" +
                "ts TIMESTAMP, \n" +
                "i_re_report_flag BOOL,\n" +
                "i_location_state TINYINT,\n" +
                "i_latitude_state TINYINT,\n" +
                "i_longitude_state TINYINT,\n" +
                "i_valve_info TINYINT,\n" +
                "i_switch_info TINYINT,\n" +
                "i_tank_temperature FLOAT,\n" +
                "i_tank_pressure FLOAT,\n" +
                "i_tank_liquidLevel INT,\n" +
                "i_tank_gas_concentration FLOAT,\n" +
                "i_flowmeter FLOAT,\n" +
                "i_latitude DOUBLE, \n" +
                "i_longitude DOUBLE, \n" +
                "i_altitude DOUBLE, \n" +
                "i_speed FLOAT, \n" +
                "i_direction SMALLINT\n" +
                ") TAGS(productKey binary(32),deviceCode binary(32));\n";
        try {
            Long id = TaosUtil.execute(str);
        } catch (SQLException e) {
            log.error("SQLException." + e);
        }

        String str2 = "create table if not exists t_gps_" + productKey + "_" + deviceCode + " " +
                "USING t_gps_data TAGS('" + productKey + "', '" + deviceCode + "')";

        try {
            Long id = TaosUtil.execute(str2);
        } catch (SQLException e) {
            log.error("SQLException." + e);
        }
    }

    public static void createTankTable(String productKey,String deviceCode){
        String str = "CREATE TABLE if not exists t_tank_data (\n" +
                "ts TIMESTAMP, \n" +
                "i_tank_temperature DOUBLE, \n" +
	            "i_tank_pressure DOUBLE, \n" +
	            "i_tank_liquidLevel INT, \n" +
	            "i_tank_gas_concentration DOUBLE, \n" +
	            "i_supply_pressure DOUBLE,\n" +
	            "i_switch_info TINYINT,\n" +
	            "i_rssi INT\n" +
                ") TAGS(productKey binary(32),deviceCode binary(32));\n";
        try {
            Long id = TaosUtil.execute(str);
        } catch (SQLException e) {
            log.error("SQLException." + e);
        }

        String str2 = "create table if not exists t_tank_" + productKey + "_" + deviceCode + " " +
                "USING t_tank_data TAGS('" + productKey + "', '" + deviceCode + "')";

        try {
            Long id = TaosUtil.execute(str2);
        } catch (SQLException e) {
            log.error("SQLException." + e);
        }
    }


}
