package com.messageprocessingapp.listeners;

import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.repository.MessageRepository;
import com.messageprocessingapp.utils.AppLogger;
import com.messageprocessingapp.utils.QueueMessageProcessing;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {

    IMessageRepository messageRepository = new MessageRepository();
    List<MessageType> totalSubTables = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            AppLogger.setLogger();

            // assign users to subtables
            // creating threads and assigning to all subtables

            List<MessageType> typeOneMsgs = messageRepository.getMessageType("TypeOne");
            List<MessageType> typeTwoMsgs = messageRepository.getMessageType("TypeTwo");

            totalSubTables.addAll(typeOneMsgs);
            totalSubTables.addAll(typeTwoMsgs);

            for(MessageType type : totalSubTables) {
                Thread t = new Thread(new QueueMessageProcessing());
                t.setName(type.getSub_table_name());
//            QueueMessageProcessing.threadsMap.put(t.getName(), type.getSub_table_name());
                t.start();
            }
//        System.out.println(QueueMessageProcessing.threadsMap);

//        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(totalSubTables.size());
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                totalSubTables.size(),
//                totalSubTables.size(),
//                5,
//                TimeUnit.SECONDS,
//                queue
//        );
//
//        for(MessageType type : totalSubTables) {
//            executor.execute(new QueueMessageProcessing(type.getSub_table_name()));
//        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
