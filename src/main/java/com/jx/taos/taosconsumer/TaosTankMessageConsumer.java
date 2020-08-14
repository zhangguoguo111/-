package com.jx.taos.taosconsumer;

import com.jx.taos.dto.TankDataDto;
import com.jx.taos.utils.TaosTableUtil;
import com.jx.taos.utils.TaosUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.jx.message.JxMessage;
import com.jx.message.device2platform.TankReportData;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

/**
 * 处理储罐消息 0x0210
 *
 * @author lixingao 2020-07-09 17:03
 */
@Component
public class TaosTankMessageConsumer extends MessageConsumer<JxMessage> {
    private Logger log = LoggerFactory.getLogger(getClass());


    private CBORFactory cborFactory = new CBORFactory();
    private ObjectMapper objectMapper = new ObjectMapper(cborFactory);


    public TaosTankMessageConsumer() {
        super(20000, "TaosTankMessageConsumer", 500, 2);
    }

    @Override
    protected void work(List<JxMessage> list){
        // key：表名，value：储罐数据列表
        Map<String, List<TankDataDto>> tankMap = new HashMap<>();

        // 下面这个for循环是为了组装上面这个数据结构
        for(JxMessage message:list){
            if(message == null
                    || StringUtils.isBlank(message.getProductKey())
                    || StringUtils.isBlank(message.getDeviceCode())){
                continue;
            }

            try{
                TankReportData tankReportData = message.getJSONContent(TankReportData.class);
                if(tankReportData == null){
                    log.warn("TankReportData analyzed from message {} is null, message will be discard.", message.getMessageSequence());
                    continue;
                }

                String tableName = TaosTableUtil.getTankDataTableName(message.getProductKey(),message.getDeviceCode());
                tankMap.putIfAbsent(tableName, new ArrayList<>());

                TankDataDto tankDataDto = new TankDataDto(  message.getMessageSequence(),
                                                            message.getProductKey(),
                                                            message.getDeviceCode(),
                                                            tankReportData);
                tankMap.get(tableName).add(tankDataDto);

            }catch (Exception e){
                log.error("Build tank data map failed.", e);
            }
        }

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        for(String key : tankMap.keySet()){
            stringBuilder.append(key +" VALUES ");
            List<TankDataDto> dtoList = tankMap.get(key);
            for (TankDataDto dto : dtoList){
                stringBuilder.append(" ('");

                    stringBuilder.append(dto.getCollectTime()+ "'");


                    stringBuilder.append(", "+dto.getTemperature());


                    stringBuilder.append(", "+dto.getPressure());


                    stringBuilder.append(", "+dto.getLiquidLevel());


                    stringBuilder.append(", "+dto.getGasConcentration());


                    stringBuilder.append(", "+dto.getSupplyPressure());


                    stringBuilder.append(", "+dto.getSwitchInfo());


                    stringBuilder.append(", "+dto.getRssi());

                stringBuilder.append(") ");
            }
        }
        try {
            Long id = TaosUtil.execute(stringBuilder.toString());
            log.info("insert succeed");
        } catch (SQLException e) {
            log.error("SQLException."+e);
        }
    }

}
