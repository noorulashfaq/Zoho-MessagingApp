package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageProcessRepository;
import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageProcess;
import com.messageprocessingapp.repository.MessageProcessRepository;
import com.messageprocessingapp.repository.MessageRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QueueMessageProcessing implements Runnable {
    IMessageRepository messageRepository = new MessageRepository();
    IMessageProcessRepository messageProcessRepository = new MessageProcessRepository();

    @Override
    public void run() {
        Properties prop = new Properties();

        try {
            prop.load(new FileReader("F:/Java/MessageProcessingApp/config.properties"));
//            while(!Thread.currentThread().isInterrupted()){
            while (true) {
                try {
                    String threadName = Thread.currentThread().getName();
                    if (messageRepository.checkIfTableIsNotEmpty(threadName)) {
                        Message message = messageRepository.getOldestMessage(threadName);

                        if (message != null) {
                            MessageProcess messageProcess = new MessageProcess();
                            messageProcess.setThread_id(threadName);
                            messageProcess.setUser_id(message.getUser_id());
//                        messageProcess.setMessage_content(new String(message.getMessage_content().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
//                            if(message.getMessage_content().substring(message.getMessage_content().length()-4).equals(".txt")){
//                                FileReader fr = new FileReader(new File("F:/Java/MessageProcessingApp/user_message_files/" + message.getMessage_content()));
//                                StringBuilder sb = new StringBuilder();
//                                while (fr.read() != -1){
//                                    sb.append(fr.read());
//                                }
//                                messageProcess.setMessage_content(sb.toString());
//                            }else{
//                                messageProcess.setMessage_content(message.getMessage_content());
//                            }
                            messageProcess.setMessage_content(message.getMessage_content());
                            messageProcess.setContent_length(messageProcess.getMessage_content().getBytes(StandardCharsets.UTF_8).length);
                            messageProcess.setMessage_type(message.getMessage_type());
                            messageProcess.setPriority(message.getPriority());
                            messageProcess.setPosted_at(message.getPosted_at());
                            messageProcess.setMessage_id(message.getMessage_id());

                            Thread.sleep(Integer.parseInt(prop.getProperty("THREAD_SLEEP_TIME")));

                            MessageProcess mp = messageProcessRepository.
                                    addMessageProcess(messageProcess);
                            AppLogger.logger.info(Thread.currentThread().getName() + " is working on " + message.getMessage_id() + " in table " + Thread.currentThread().getName());

                            if (mp != null) {
                                boolean b = messageRepository.updateStatus(threadName, message.getMessage_id());
                                if (b) {
                                    AppLogger.logger.info("Message " + message.getMessage_id() + " in " + Thread.currentThread().getName() + " has been processed");
                                } else {
                                    AppLogger.logger.info("Message " + message.getMessage_id() + " in " + Thread.currentThread().getName() + " processing failed");
                                }
                            } else {
                                throw new RuntimeException("Message processing failed");
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
