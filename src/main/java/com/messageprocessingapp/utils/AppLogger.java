package com.messageprocessingapp.utils;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
    public static final Logger logger = Logger.getLogger("BookstoreLogger");

    public static void setLogger() throws IOException {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        FileHandler fh = new FileHandler("F:/Java/MessageProcessingApp/app_logs/messagingapp.log", true);
        fh.setFormatter(new SimpleFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(fh);
    }
}
