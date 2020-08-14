package com.jx.taos.taosconsumer;

import com.alibaba.fastjson.JSON;
import com.jx.taos.dto.GeneralDataDto;
import com.jx.taos.utils.TaosTableUtil;
import com.jx.taos.utils.TaosUtil;
import com.jx.message.JxMessage;
import com.jx.message.constans.MessageType;
import com.jx.message.device2platform.BatchGeneralInfo;
import com.jx.message.device2platform.GeneralInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

/**
 * 处理综合信息存储 0x0200,0x0201
 *
 * @author lixingao 2020-07-09 17:03
 */
@Component
public class TaosGpsMessageConsumer extends MessageConsumer<JxMessage> {
    private Logger log = LoggerFactory.getLogger(getClass());


    public TaosGpsMessageConsumer() {
        super(20000, "TaosGpsMessageConsumer", 500, 2);
    }

    @Override
    protected void work(List<JxMessage> list){

        // key：表名，value：综合信息列表
        Map<String, List<GeneralDataDto>> dataMap = new HashMap<>();

        // 下面这个for循环是为了组装上面这个数据结构
        for(JxMessage message:list){
            if(message == null
                    || StringUtils.isBlank(message.getProductKey())
                    || StringUtils.isBlank(message.getDeviceCode())){
                continue;
            }
            try{
                String tableName = TaosTableUtil.getGpsTableName(message.getProductKey(),message.getDeviceCode());
                dataMap.putIfAbsent(tableName, new ArrayList<>());

                if(message.getMessageType().equals(MessageType.general_info.getMessageType())){

                    GeneralInfo generalInfo = message.getJSONContent(GeneralInfo.class);
                    if(generalInfo == null){
                        log.warn("GeneralInfo analyzed from message {} is null, message will be discard.",
                                message.getMessageSequence());
                        continue;
                    }


                    GeneralDataDto generalDataDto = new GeneralDataDto( message.getMessageSequence(),
                                                                        message.getMessageType(),
                                                                        message.getProductKey(),
                                                                        message.getDeviceCode(),
                                                                        generalInfo);
                    dataMap.get(tableName).add(generalDataDto);

                }else if(message.getMessageType().equals(MessageType.batch_general_info.getMessageType())){
                    BatchGeneralInfo batchGeneralInfo = message.getJSONContent(BatchGeneralInfo.class);

                    if(batchGeneralInfo == null){
                        log.warn("BatchGeneralInfo analyzed from message {} is null, message will be discard.", message.getMessageSequence());
                        continue;
                    }

                    GeneralDataDto generalDataDto = null;
                    for(int i=0; i<batchGeneralInfo.getDataList().size(); i++){
                        GeneralInfo info = batchGeneralInfo.getDataList().get(i);
                        generalDataDto = new GeneralDataDto(    batchGeneralInfo.getDataType(),
                                                                message.getMessageType(),
                                                                message.getMessageSequence(),
                                                                message.getProductKey(),
                                                                message.getDeviceCode(),
                                                                info);
                        dataMap.get(tableName).add(generalDataDto);
                    }

                    log.error("Error message type, message will be discard. {}", JSON.toJSONString(message));
                }
            }catch (Exception e){
                log.error("Build general data map failed.", e);
            }
        }


        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        for(String key : dataMap.keySet()){
                stringBuilder.append(key +" VALUES ");
                List<GeneralDataDto> dtoList = dataMap.get(key);
                for (GeneralDataDto dto : dtoList){
                    stringBuilder.append(" ('");

                        stringBuilder.append(dto.getCollectTime()+"'");

                        stringBuilder.append(", "+dto.getReReportFlag());

                        stringBuilder.append(", "+dto.getLocationState());

                        stringBuilder.append(", "+dto.getLatitudeState());

                        stringBuilder.append(", "+dto.getLongitudeState());


                        stringBuilder.append(", "+dto.getValveInfo());

                        stringBuilder.append(", "+dto.getSwitchInfo());


                        stringBuilder.append(", "+dto.getTemperature());


                        stringBuilder.append(", "+dto.getPressure());


                        stringBuilder.append(", "+dto.getLiquidLevel());


                        stringBuilder.append(", "+dto.getGasConcentration());


                        stringBuilder.append(", "+dto.getFlowmeter());


                        stringBuilder.append(", "+dto.getLatitude());


                        stringBuilder.append(", "+dto.getLongitude());


                        stringBuilder.append(", "+dto.getAltitude());


                        stringBuilder.append(", "+dto.getSpeed());


                        stringBuilder.append(", "+dto.getDirection());

                    stringBuilder.append(") ");
                }
            }
        try {
            Long id = TaosUtil.execute(stringBuilder.toString());
        } catch (SQLException e) {
            log.error("SQLException."+e);
        }
        log.info("insert succeed");
    }
}


