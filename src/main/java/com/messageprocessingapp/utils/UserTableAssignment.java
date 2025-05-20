package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageRepository;

import java.util.Comparator;
import java.util.List;

public class UserTableAssignment {
    static IMessageRepository messageRepository = new MessageRepository();

    public static String getAssignedTable(int user_id, String message_type){
        List<MessageType> subTableList = messageRepository.getMessageType(message_type);

        if(subTableList == null || subTableList.isEmpty()){
            throw new RuntimeException("No sub-tables found for message type: " + message_type);
        }

        subTableList.sort(Comparator.comparing(messageType -> messageType.getSub_table_name()));

        int index = user_id % subTableList.size();

        return subTableList.get(index).getSub_table_name();
    }

}
