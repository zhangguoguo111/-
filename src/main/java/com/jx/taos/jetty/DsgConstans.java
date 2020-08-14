package com.jx.taos.jetty;


import java.util.UUID;

/**
 * 常量
 */
public class DsgConstans {

    /**
     * 在线心跳间隔
     */
    public static final Integer HEAR_BEAT_INTERVAL = 30 * 1000;

    /**
     * 同步设备在线状态任务间隔
     */
    public static final Integer SYNC_DEVICE_ONLINE_STATUS_INTERVAL = 10 * 60 * 1000;

    /**
     * 设备在线状态
     * 0：不在线，1：在线，2：休眠
     */
    public static final Integer DEVICE_OFFLINE = 0;
    public static final Integer DEVICE_ONLINE = 1;
    public static final Integer DEVICE_SLEEP = 2;


    /**
     * 是否启动监控页面
     */
    public static final String START_STATUS = "1";


    public static final String SESSION_USER = UUID.randomUUID().toString().replace("-", "");

    public static final String ATTACH_PRODUCT_KEY_REDIS_KEY = "ATTACH_REDIS_";
    /**
     * -1：永久缓存
     * 0：不保存
     */
    public static final Integer DATA_STORAGE_TIME_PERMANENT = -1;
    public static final Integer DATA_STORAGE_TIME_NO_SAVE = 0;
}
