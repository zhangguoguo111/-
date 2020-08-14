package com.jx.taos.utils;


import com.jx.taos.druid.Druid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;



/**
 * 测试涛思时序数据库
 *
 * @author yusijia 2020-07-01 11:58
 */

public class TaosUtil {

    private static final Logger logger = LoggerFactory.getLogger(TaosUtil.class);
    private static Connection conn;

    /**
     * 执行Sql
     *
     * @param sql
     * @return 插入操作时，返回新增ID
     * @throws SQLException
     */
    public static Long execute(String sql) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("com.cheaxsy.practies.taos.DruidUtil execute sql is [{}]", sql);
        }
        Statement stmt = null;
        try {
            conn = Druid.INSTANCE.getConnection();
            stmt = conn.createStatement();
            stmt.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(stmt, conn);
        }
        return  null;
    }


    /**
     * 关闭全部连接
     */
    public static void closeAll(Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Close Statement failed.", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Close Connection failed.", e);
            }
        }
    }
}
