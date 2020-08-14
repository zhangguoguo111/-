package com.jx.taos.consts;

import com.jx.mns.manager.PropertyManager;

/**
 * 系统级常量
 *
 * @author yusijia 2020-05-08 10:03
 */
public class SystemConsts {
    public static final String SYSTEM_NAME = "TAOSDATA";
    public static final String ALIAS = PropertyManager.INSTANCE.getProperty("system.alias");
    public static final String USERNAME = ALIAS;
    public static final String SECRET = "XICHENXIENHAPPYFOREVER";

    public static String getSystemName() {
        return SYSTEM_NAME;
    }

    public static String getALIAS() {
        return ALIAS;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getSECRET() {
        return SECRET;
    }

    //    public static final String dbPropertiesFilePath = "sysparams/db.properties";
//    public static final String systemPropertiesFilePath = "sysparams/system.properties";
//    public static final String cachePropertiesFilePath = "sysparams/cache.properties";
}
