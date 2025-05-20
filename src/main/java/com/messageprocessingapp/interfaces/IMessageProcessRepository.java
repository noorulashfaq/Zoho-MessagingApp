package com.messageprocessingapp.interfaces;

import com.messageprocessingapp.models.MessageProcess;

import java.sql.Timestamp;

public interface IMessageProcessRepository {
    MessageProcess addMessageProcess(MessageProcess mp);
//    boolean updateThreadId(String threadId, Timestamp p_id);
}
