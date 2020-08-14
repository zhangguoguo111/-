package com.jx.taos.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.taosdata.jdbc.TSDBDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

/**
 * Druid 数据库连接池工具类
 *
 * @author lixingao 2018-07-14 20:39
 */
public enum Druid {


    /**
     * dataSource客户端实现
     */
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(Druid.class);
    /**
     * 连接池对象
     */
    private  DruidDataSource ds;

    public  void initDb() {
        try {
            Properties properties = new Properties();
            properties.put("driverClassName","com.taosdata.jdbc.TSDBDriver");
            properties.put("url","jdbc:TAOS://192.168.1.36:6030/db_lxa");
            properties.put("username","root");
            properties.put("password","taosdata");

            properties.put(TSDBDriver.PROPERTY_KEY_USER, "root");
            properties.put(TSDBDriver.PROPERTY_KEY_PASSWORD, "taosdata");
            properties.put(TSDBDriver.PROPERTY_KEY_CONFIG_DIR, "C:/idea/TDengine/TDengine/cfg");
            properties.put(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            properties.put(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            properties.put(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

            properties.put("maxActive","10");
            properties.put("initialSize","3");
            properties.put("maxWait","10000");
            properties.put("minIdle","3");
            properties.put("timeBetweenEvictionRunsMillis","3000");
            properties.put("minEvictableIdleTimeMillis","60000");
            properties.put("maxEvictableIdleTimeMillis","90000");
            properties.put("validationQuery","describe log.dn");
            properties.put("testWhileIdle","true");
            properties.put("testOnBorrow","false");
            properties.put("testOnReturn","false");

            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            logger.error("初始化 Druid 连接错误，请检查配置文件！", e);
        }
    }

    /**
     * 从池中获取一个连接
     */
    public  Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
