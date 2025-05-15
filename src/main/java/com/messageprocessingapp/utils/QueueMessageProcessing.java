package com.messageprocessingapp.utils;

import com.messageprocessingapp.interfaces.IMessageProcessRepository;
import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageProcess;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageProcessRepository;
import com.messageprocessingapp.repository.MessageRepository;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

class QueueMessageProcessing implements Runnable{
    IMessageRepository messageRepository = new MessageRepository();
    IMessageProcessRepository messageProcessRepository = new MessageProcessRepository();
    @Override
    public void run() {
        try{
//            while(messageRepository.checkIfTableIsNotEmpty(ThreadsCreator.threadsMap.get(Thread.currentThread().getName())));{
                Message message = messageRepository.getOldestMessage(ThreadsCreator.threadsMap.get(Thread.currentThread().getName()));

                if(message != null){
                    MessageProcess messageProcess = new MessageProcess();
                    messageProcess.setThread_id(Thread.currentThread().getName());
                    messageProcess.setUser_id(message.getUser_id());
                    messageProcess.setMessage_content(message.getMessage_content());
                    messageProcess.setMessage_type(message.getMessage_type());
                    messageProcess.setPriority(message.getPriority());
                    messageProcess.setPosted_at(message.getPosted_at());
                    messageProcess.setThread_id(Thread.currentThread().getName()+"-"+ThreadsCreator.threadsMap.get(Thread.currentThread().getName()));

                    boolean result = messageProcessRepository.addMessageProcess(messageProcess);
                    System.out.println(Thread.currentThread().getName() + " is working on " + message.getMessage_id() + " in table " + ThreadsCreator.threadsMap.get(Thread.currentThread().getName()));

                    if(result){
                        boolean b = messageRepository.deleteMessage(ThreadsCreator.threadsMap.get(Thread.currentThread().getName()), message.getMessage_id());
                        if(b){
                            System.out.println("Message deleted successfully");
                        }else{
                            System.out.println("Message not deleted");
                        }
                    }else{
                        throw new RuntimeException("Message processing failed");
                    }
                }
//            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
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
