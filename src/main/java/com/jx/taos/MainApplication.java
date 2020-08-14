package com.jx.taos;

import com.jx.mns.common.consts.RedisKeyConsts;
import com.jx.mns.utils.redis.RedisUtil;
import com.jx.taos.consts.SystemConsts;
import com.jx.taos.dto.EquipmentAuthInfo;
import com.jx.taos.jetty.Monitor;
import com.jx.taos.session.MqttSession;
import com.jx.taos.druid.Druid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixingao
 * @date 2020/7/9 10:16
 */

@Configuration
@SpringBootApplication(scanBasePackages = {"com.jx.taos","com.jx.mns.utils"})
public class MainApplication {
    public static void main(String[] args) throws Exception{
        new SpringApplicationBuilder(MainApplication.class).run(args);

        MqttSession.INSTANCE.init();
        Druid.INSTANCE.initDb();
        initMqttConnectAuthInfo();

        new Monitor().start();
    }

    private static void initMqttConnectAuthInfo(){
        EquipmentAuthInfo authInfo = new EquipmentAuthInfo();
        authInfo.setDeviceCode(SystemConsts.USERNAME);
        authInfo.setDeviceSecret(SystemConsts.SECRET);
        authInfo.setProductKey(SystemConsts.SYSTEM_NAME);
        authInfo.setProductSecret(SystemConsts.SECRET);

        String clientId = authInfo.getProductKey() + "-" + authInfo.getDeviceCode();
        RedisUtil.hset(RedisKeyConsts.DEVICE_AUTH_STORE, clientId, authInfo);
    }


}
