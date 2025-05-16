package com.messageprocessingapp.utils;

import com.messageprocessingapp.exceptions.SizeLimitExceededException;
import com.messageprocessingapp.models.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class MessageEncoder {
    public static String encode(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decode(String input) {
        byte[] bytes = Base64.getDecoder().decode(input);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String getMessageMetaData(Message message, String tableName) throws IOException, SizeLimitExceededException {
        Properties prop = new Properties();
        prop.load(new FileReader("F:/Java/MessageProcessingApp/config.properties"));
        if(message.getMessage_content().length() <= Integer.parseInt(prop.getProperty("ACCEPTABLE_MESSAGE_SIZE"))) {
            return message.getMessage_content();
        }else if(message.getMessage_content().length() > Integer.parseInt(prop.getProperty("ACCEPTABLE_MESSAGE_SIZE")) && message.getMessage_content().length() <= Integer.parseInt(prop.getProperty("ACCEPTABLE_FILE_MESSAGE_SIZE"))){
//            thread id, user id, msgtype
            String fileName = message.getMessage_type() + "_" + message.getUser_id() + "_" + tableName + ".txt";
            File file = new File("F:/Java/MessageProcessingApp/user_message_files/" + fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(message.getMessage_content());
            fw.write("\n");
            return fileName;
        }else{
            throw new SizeLimitExceededException("Size limit reached");
        }
    }
}
