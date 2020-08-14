package com.jx.taos.session;

import com.alibaba.fastjson.JSON;
import com.jx.mns.utils.SpringContextHolder;
import com.jx.taos.consts.SystemConsts;
import com.jx.taos.taosconsumer.TaosGpsMessageConsumer;
import com.jx.taos.taosconsumer.TaosTankMessageConsumer;
import com.jx.message.JxMessage;
import com.jx.message.constans.MessageType;
import com.jx.mns.common.consts.OnlineStatus;
import com.jx.mns.common.consts.RedisKeyConsts;

import com.jx.mns.exception.ProgramException;
import com.jx.mns.manager.PropertyManager;

import com.jx.mns.utils.Sha256Util;

import com.jx.mns.utils.redis.RedisUtil;
import com.jx.msg.mqtt.JxMqttClient;
import com.jx.msg.mqtt.interfaces.MessageCallback;
import com.jx.msg.mqtt.utils.ParamManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lixingao
 * @date 2020/7/8 16:15
 */
public enum MqttSession {
    /**
     * Singleton实现
     * */
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(getClass());

    private JxMqttClient client;

    private TaosGpsMessageConsumer taosGpsMessageConsumer = SpringContextHolder.getBean("taosGpsMessageConsumer");
    private TaosTankMessageConsumer taosTankMessageConsumer = SpringContextHolder.getBean("taosTankMessageConsumer");


    private ExecutorService executorService;

    /**
     * 是否要对收到的数据进行处理
     */
    private Boolean dealDataFlag = false;


    public void init(){
        this.executorService = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        String clientId = SystemConsts.SYSTEM_NAME + "-" + SystemConsts.USERNAME;

        ParamManager params = new ParamManager();
        params.setClientId(clientId);
        params.setMqttHost(PropertyManager.INSTANCE.getProperty("mqtt.host"));
        params.setMqttUserName(SystemConsts.USERNAME);

        // 初始化是否处理数据的标记
        dealDataFlag = "yes".equals(PropertyManager.INSTANCE.getProperty("system.connect.to.broker"));

        try{
            String password = Sha256Util.convertToSha256String(SystemConsts.SECRET + SystemConsts.USERNAME.substring(SystemConsts.USERNAME.length()-1));
            params.setMqttPassword(password);
            params.setQos(Integer.parseInt(PropertyManager.INSTANCE.getProperty("mqtt.qos")));
            params.setMessageCallback( new MqttMessageCallback());

            String[] topics = new String[1];
            // 为了负载均衡，使用hiveMQ共享主题
            // 一个group有多个节点共同订阅一个共享主题，该group组内只有一个节点会收到订阅消息
            // 多个group，每个group都有多个节点订阅共享主题，则每个group都会有1个节点收到订阅消息
            topics[0] = "$share/group-taosData/device/+/+/data";
            params.setSubscribeTopics(topics);

            client = new JxMqttClient(params);
        }catch (NoSuchAlgorithmException e){
            log.error("Can't find algorithm, init mqtt client failed.");
        }
    }

    public JxMqttClient getClient(){
        if(this.client == null){
            throw new ProgramException("system.mqtt.client.not.initialized", "Mqtt客户端尚未初始化");
        }

        return this.client;
    }

    class MqttMessageCallback implements MessageCallback {

        @Override
        public void messageArrived(String topicName, JxMessage jxMessage){
            log.debug("A message from topic {} is arrived.{}", topicName, JSON.toJSONString(jxMessage));

            if(dealDataFlag){
                executorService.submit(new MessageProcessor(jxMessage));
            }else{
                log.debug("dealDataFlag is {}, don't need to deal receive message.", dealDataFlag);
            }
        }
    }

    class MessageProcessor implements Runnable{
        private JxMessage jxMessage;

        public MessageProcessor(JxMessage jxMessage){
            this.jxMessage = jxMessage;
        }

        @Override
        public void run() {
            MessageType messageType = MessageType.getEnumByMessageType(jxMessage.getMessageType());

            if(jxMessage == null
                    || StringUtils.isBlank(jxMessage.getProductKey())
                    || StringUtils.isBlank(jxMessage.getDeviceCode())){
                log.error("Message content is error, it will be discard. {}", JSON.toJSONString(jxMessage));
                return;
            }
            // 收到设备任何消息时，刷新该设备的在线状态缓存
            RedisUtil.setex(RedisKeyConsts.DEVICE_ONLINE_STATUS_PREFIX + jxMessage.getProductKey() + "_" + jxMessage.getDeviceCode(),
                    RedisKeyConsts.DEVICE_ONLINE_STATUS_EFFECTIVE_TIME, OnlineStatus.ONLINE.getState());

            try{
                switch (messageType){
                    case general_info:
                    case batch_general_info:
                        taosGpsMessageConsumer.offer(jxMessage);
                        break;
                    case tank_info:
                        taosTankMessageConsumer.offer(jxMessage);
                        break;
                    default:
                        log.debug("Can't find property message consumer, message will be discard. {}", JSON.toJSONString(jxMessage));

                }
            }catch (Exception e){
                log.error("Offer message failed.", e);
            }
        }
    }

}
