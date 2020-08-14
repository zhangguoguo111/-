package com.jx.taos.service;

import org.springframework.stereotype.Service;

/**
 * @author lixingao
 * @date 2020/7/16 9:29
 */

public interface CreateTableService {
    /**
     *
     * @param productKey
     * @param deviceCode
     * @param messageType
     */
    void createTable(String productKey,String deviceCode,Short messageType);


}
