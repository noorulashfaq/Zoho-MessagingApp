package com.messageprocessingapp.interfaces;

import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageType;

import java.util.List;

public interface IMessageRepository {
    boolean addMessage(Message message);
    Message getMessage(int messageId);
    List<MessageType> getMessageType(String messageType);
    int getNumberOfMessages(String subtype);
    boolean checkMessageFromUserAlreadyExists(String tableName, int user_id);
    Message getRowsFromTable(String tableName);

    Message getOldestMessage(String tableName);
    boolean deleteMessage(String tableName, int messageId);
    boolean checkIfTableIsNotEmpty(String subtables);
}
