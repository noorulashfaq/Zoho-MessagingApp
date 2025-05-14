package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDistribution {
    IMessageRepository messageRepository = new MessageRepository();
    public String findTableName(Message message){

//        if(message.getMessage_type().equals("TypeOne")){
            List<MessageType> subTableData = messageRepository.getMessageType(message.getMessage_type());

            // checking whether message from this user id already exists
            // getting number of rows in each sub tables
            String tableName = null;
            Map<String, Integer> numberOfRows = new HashMap<>();
            for(MessageType mt: subTableData){
                tableName = messageRepository.checkMessageFromUserAlreadyExists(mt.getSub_table_name(), message.getUser_id()) ? mt.getSub_table_name() : null;
                if(tableName != null){
                    return tableName;
                }
                numberOfRows.put(mt.getSub_table_name(), messageRepository.getNumberOfMessages(mt.getSub_table_name()));
            }

            String tableWithMinValue = null;
            int minCount = Integer.MAX_VALUE;
            for(String table: numberOfRows.keySet()){
                if(numberOfRows.get(table) < minCount){
                    minCount = Math.min(minCount, numberOfRows.get(table));
                    tableWithMinValue = table;
                }
            }

            return tableWithMinValue;
//        }else if(message.getMessage_type().equals("TypeTwo")){
//            List<MessageType> subTableData = messageRepository.getMessageType(message.getMessage_type());
//
//
//
//        }
//        return null;

    }



}
