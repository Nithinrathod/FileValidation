package com.tcs.docwatcher.config;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    private static final Logger logger = Logger.getLogger("DocWatcher");

    public static Logger getLogger() throws IOException {

        logger.setUseParentHandlers(false);

        FileHandler fh = new FileHandler("logs/document_watcher.log", true);
        fh.setFormatter(new SimpleFormatter());

        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new SimpleFormatter());

        logger.addHandler(fh);
        logger.addHandler(ch);

        return logger;
    }
}
