package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageRepository;

import java.util.*;

public class ThreadsCreator {

    static IMessageRepository messageRepository = new MessageRepository();
    static Map<String, String> threadsMap = new HashMap<>();
    static List<MessageType> totalSubTables = new ArrayList<>();

    static {
        List<MessageType> typeOneMsgs = messageRepository.getMessageType("TypeOne");
        List<MessageType> typeTwoMsgs = messageRepository.getMessageType("TypeTwo");

        totalSubTables.addAll(typeOneMsgs);
        totalSubTables.addAll(typeTwoMsgs);
    }

    public void startThreads(){
        for(MessageType type : totalSubTables) {
            Thread t = new Thread(new QueueMessageProcessing());
            threadsMap.put(t.getName()+"-"+type.getSub_table_name(), type.getSub_table_name());
            t.start();
        }

    }

}
