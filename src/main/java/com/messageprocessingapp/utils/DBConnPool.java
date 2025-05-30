package com.messageprocessingapp.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DBConnPool {
    private static BlockingQueue<Connection> pool = new LinkedBlockingQueue<>();

    static{
        try{
            Properties prop = new Properties();
            prop.load(new FileReader("F:/Java/MessageProcessingApp/config.properties"));
            Class.forName(prop.getProperty("DB_DRIVER"));

            for(int i = 0; i < Integer.parseInt(prop.getProperty("DB_POOL_SIZE_MAX")); i++){
                pool.add(DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("DB_USER"), prop.getProperty("DB_PASS")));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConn() throws InterruptedException {
        return pool.take();
    }

    public static void releaseConnection(Connection connection){
        if(connection != null){
            pool.offer(connection);
        }
    }
}


