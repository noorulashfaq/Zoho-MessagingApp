package com.messageprocessingapp.interfaces;

import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageResponse;
import com.messageprocessingapp.models.MessageType;

import java.util.List;

public interface IMessageRepository {
    MessageResponse addMessage(Message message);
    List<MessageType> getMessageType(String messageType);
    int getNumberOfMessages(String subtype);
    boolean checkMessageFromUserAlreadyExists(String tableName, int user_id);

//    Message getRowsFromTable(String tableName);
//    Message getMessage(int messageId);
//    boolean deleteMessage(String tableName, int messageId);

    boolean updateStatus(String tableName, int message_id);
    Message getOldestMessage(String tableName);
    boolean checkIfTableIsNotEmpty(String subtables);
}
