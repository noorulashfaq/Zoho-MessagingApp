package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageProcessRepository;
import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.listeners.AppContextListener;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageProcess;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageProcessRepository;
import com.messageprocessingapp.repository.MessageRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueMessageProcessing implements Runnable{
    IMessageRepository messageRepository = new MessageRepository();
    IMessageProcessRepository messageProcessRepository = new MessageProcessRepository();
    public static Map<String, String> threadsMap = new HashMap<>();

//    String tableName;
//    public QueueMessageProcessing(String tableName){
//        this.tableName = tableName;
//    }

    @Override
    public void run() {
        try{
//            while(!Thread.currentThread().isInterrupted()){
            while(true){
//                System.out.println(Thread.currentThread().getName());
                try{
                    String threadName = Thread.currentThread().getName();
                    if(messageRepository.checkIfTableIsNotEmpty(threadName)) {
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

                            boolean result = messageProcessRepository.addMessageProcess(messageProcess);
                            System.out.println(Thread.currentThread().getName() + " is working on " + message.getMessage_id() + " in table " + Thread.currentThread().getName());

                            if (result) {
                                boolean b = messageRepository.deleteMessage(threadName, message.getMessage_id());
                                if (b) {
                                    System.out.println("Message deleted successfully");
                                } else {
                                    System.out.println("Message not deleted");
                                }
//                                break;
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
                }
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

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

//public class QueueMessageProcessing {
//    IMessageRepository messageRepository = new MessageRepository();
//    IMessageProcessRepository messageProcessRepository = new MessageProcessRepository();
//
//    public void assignThreadsToTables(){
//        ThreadPoolExecutor executor = ThreadPoolCreator.getThreadPoolExecutor();
//
//        List<MessageType> typeOneMsgs = messageRepository.getMessageType("TypeOne");
//        List<MessageType> typeTwoMsgs = messageRepository.getMessageType("TypeTwo");
//
//        List<MessageType> totalSubTables = new ArrayList<>();
//        totalSubTables.addAll(typeOneMsgs);
//        totalSubTables.addAll(typeTwoMsgs);
//
////        Map<String, String> threadAndTable = new HashMap<>();
//
////        for (int i = 0; i < totalSubTables.size(); i++) {
////            int indexVal = i;
////            executor.execute(()->{
////                threadAndTable.put(Thread.currentThread().getName(), totalSubTables.get(indexVal).getSub_table_name());
////            });
////        }
//        while(messageRepository.checkIfTableIsNotEmpty(totalSubTables)) {
//            executor.execute(() -> {
//                try {
////                System.out.println(Thread.currentThread().getName());
//                    Message currentMessage = null;
//                    String processingTableName = null;
//
//                    Timestamp minTimestamp = new Timestamp(System.currentTimeMillis());
//                    for (int i = 0; i < totalSubTables.size(); i++) {
//                        Message msg = messageRepository.getRowsFromTable(totalSubTables.get(i).getSub_table_name());
//                        if (msg != null && msg.getPosted_at() != null && msg.getPosted_at().before(minTimestamp)) {
//                            currentMessage = msg;
//                            minTimestamp = msg.getPosted_at();
//                            processingTableName = totalSubTables.get(i).getSub_table_name();
//                        }
//                    }
//
//                    if (currentMessage != null && processingTableName != null) {
//                        MessageProcess mp = new MessageProcess();
//
//                        mp.setUser_id(currentMessage.getUser_id());
//                        mp.setMessage_content(currentMessage.getMessage_content());
//                        mp.setContent_length(currentMessage.getMessage_content().length());
//                        mp.setMessage_type(currentMessage.getMessage_type());
//                        mp.setPriority(currentMessage.getPriority());
//                        mp.setPosted_at(currentMessage.getPosted_at());
//
//                        boolean flag = messageProcessRepository.addMessageProcess(mp);
//                        if (flag) {
//                            messageProcessRepository.updateThreadId(Thread.currentThread().getName(), currentMessage.getPosted_at());
//                        }
//                        messageRepository.deleteMessage(processingTableName, currentMessage.getMessage_id());
//
////                        final Message finalCurrentMessage = currentMessage;
////                        final String finalProcessingTableName = processingTableName;
//
//                    }
//                    Thread.sleep(5000);
//
//                }catch (RejectedExecutionException re){
////                    throw new RuntimeException(re);
//                    System.out.println("RejectedExecutionException " + Thread.currentThread().getName());
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//        executor.shutdown();
//    }
//}
