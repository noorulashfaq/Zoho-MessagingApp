package com.messageprocessingapp.utils;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConn {
    private static Connection conn;


    public static Connection getConnection(){
        try{
            Properties props = new Properties();
            props.load(new FileReader("F:/Java/MessageProcessingApp/config.properties"));
            String url = props.getProperty("DB_URL");
            String user = props.getProperty("DB_USER");
            String pass = props.getProperty("DB_PASS");
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url, user, pass);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
