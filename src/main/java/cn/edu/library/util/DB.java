package cn.edu.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** 数据库连接工具。连接信息通过环境变量配置，避免把密码写入源码。 */
public final class DB {
    private static final String URL = env("DB_URL",
        "jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
    private static final String USER = env("DB_USER", "root");
    private static final String PASSWORD = env("DB_PASSWORD", "root123456");

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new ExceptionInInitializerError(e); }
    }

    private DB() {}

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
