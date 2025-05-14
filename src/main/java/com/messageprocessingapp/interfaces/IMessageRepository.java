package com.messageprocessingapp.interfaces;

import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageType;

import java.util.List;

public interface IMessageRepository {
    boolean addMessage(Message message);
    Message getMessage(int messageId);
    boolean deleteMessage(int messageId);
    List<MessageType> getMessageType(String messageType);
    int getNumberOfMessages(String subtype);
    boolean checkMessageFromUserAlreadyExists(String tableName, int user_id);
}
