package com.jx.taos.test;

import com.jx.taos.druid.Druid;
import com.jx.taos.service.CreateTableService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lixingao
 * @date 2020/7/16 16:44
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateTableTest {
    private static Logger log = LoggerFactory.getLogger(CreateTableTest.class);
    @Autowired
    CreateTableService createTableService;
    @Before
    public void beforeTest(){
        Druid.INSTANCE.initDb();
    }


    @Test
    public void createTable(){

//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730001", (short) 512);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730001", (short) 528);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730002", (short) 512);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730002", (short) 528);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730003", (short) 512);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730003", (short) 528);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730004", (short) 512);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730004", (short) 528);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730005", (short) 512);
//        createTableService.createTable("DCR001SCJX_1594956841247","863958040730005", (short) 528);
//
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720001", (short) 512);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720001", (short) 528);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720002", (short) 512);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720002", (short) 528);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720003", (short) 512);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720003", (short) 528);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720004", (short) 512);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720004", (short) 528);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720005", (short) 512);
//        createTableService.createTable("CCR001SCJX_1594956841247","863958040720005", (short) 528);
        for (int i=0;i<5000;i++){
            if (i<10){
                createTableService.createTable("CCR001SCJX_1595230409406","96395804073000"+i, (short) 512);
                createTableService.createTable("DCR001SCJX_1595230409406","86395804073000"+i, (short) 528);
                continue;
            }
            if (i>=10 && i<100){
                createTableService.createTable("CCR001SCJX_1595230409406","9639580407300"+i, (short) 512);
                createTableService.createTable("DCR001SCJX_1595230409406","8639580407300"+i, (short) 528);
                continue;
            }
            if (i>=100 && i<1000){
                createTableService.createTable("CCR001SCJX_1595230409406","963958040730"+i, (short) 512);
                createTableService.createTable("DCR001SCJX_1595230409406","863958040730"+i, (short) 528);
                continue;
            }
            if (i>=1000 && i<5000){
                createTableService.createTable("CCR001SCJX_1595230409406","96395804073"+i, (short) 512);
                createTableService.createTable("DCR001SCJX_1595230409406","86395804073"+i, (short) 528);
                continue;
            }
        }


    }

}
