package com.zxt.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class JDBCUtils {
    private static String driver = "oracle.jdbc.OracleDriver";
    private static String url = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static String user = "scott";
    private static String password = "admin";

    /**
     * 注册数据库驱动
     */
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            log.error("找不到驱动类：{}", ex.getMessage());
        }
    }

    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            log.error("获取数据库连接失败：{}", ex.getStackTrace());
        }
        return null;
    }

    /**
     * 资源释放
     *
     * @param conn
     * @param statement
     * @param resultSet
     */
    public static void release(Connection conn, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                log.error("关闭ResultSet失败：{}", ex.getStackTrace());
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                log.error("关闭Statement失败：{}", ex.getStackTrace());
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                log.error("关闭Connection失败：{}", ex.getStackTrace());
            }
        }
    }
}
