package com.jx.taos.test;

import com.jx.taos.druid.Druid;
import com.jx.taos.utils.TaosUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

/**
 * @author lixingao
 * @date 2020/7/20 16:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Insert {

    @Before
    public void beforeTest(){
        Druid.INSTANCE.initDb();
    }
    private Logger log = LoggerFactory.getLogger(getClass());
   // INSERT INTO t_tank_DCR001SCJX_1595230409406_863958040730001 VALUES  ('2020-07-20 16:42:32', 10.0, 881.08, 50, 1.0, 888.66, 1, 20)

    @Test
    public void insert() {
        String str = "INSERT INTO t_tank_DCR001SCJX_1595230409406_863958040730001 VALUES  ('2020-07-20 16:42:32', 10.0, 881.08, 50, 1.0, 888.66, 1, 20)";
        try {
            Long id = TaosUtil.execute(str);
        } catch (SQLException e) {
            log.error("SQLException."+e);
        }

    }






}
