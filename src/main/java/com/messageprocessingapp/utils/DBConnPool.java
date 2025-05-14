package com.messageprocessingapp.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DBConnPool {
    private static HikariDataSource dataSource;

    static{
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("F:/Java/MessageProcessingApp/config.properties"));
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("DB_URL"));
            config.setUsername(prop.getProperty("DB_USER"));
            config.setPassword(prop.getProperty("DB_PASS"));
            config.setDriverClassName(prop.getProperty("DB_DRIVER"));
            config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("DB_POOL_SIZE_MAX")));

            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HikariDataSource getDataSource(){
        return dataSource;
    }
}
