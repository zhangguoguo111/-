package com.jx.taos.utils;

import com.jx.mns.common.consts.RedisKeyConsts;
import com.jx.mns.exception.BusinessException;
import com.jx.mns.utils.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lixingao
 * @date 2020/7/10 10:35
 */
public class TaosTableUtil {
    public static String getGpsTableName(String productKey,String deviceCode) throws BusinessException {

        return "t_gps_" + productKey + "_" + deviceCode;
    }

    public static String getTankDataTableName(String productKey,String deviceCode) throws BusinessException{

        return "t_tank_" + productKey + "_" + deviceCode;
    }


}
