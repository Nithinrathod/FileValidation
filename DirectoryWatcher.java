package com.tcs.docwatcher.watcher;

import com.tcs.docwatcher.config.LoggerConfig;
import com.tcs.docwatcher.model.DocumentMeta;
import com.tcs.docwatcher.service.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class DirectoryWatcher {

    private static final Path ROOT = Paths.get("watch");
    private static final Path INCOMING = ROOT.resolve("incoming");
    private static final Path STAGING = ROOT.resolve("staging");
    private static final Path VALID = ROOT.resolve("valid");
    private static final Path INVALID = ROOT.resolve("invalid");
    private static final Path REJECTED = ROOT.resolve("rejected");
    private static final Path ARCHIVE = ROOT.resolve("archive");

    private static Logger logger;

    public static void main(String[] args) throws Exception {

        logger = LoggerConfig.getLogger();
        createDirectories();

        WatchService watchService = FileSystems.getDefault().newWatchService();
        INCOMING.register(watchService, ENTRY_CREATE);

        logger.info("Directory Watcher Started...");
        logger.info("Watching: " + INCOMING.toAbsolutePath());

        ExecutorService executor = Executors.newFixedThreadPool(5);

        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {

                Path fileName = (Path) event.context();
                Path filePath = INCOMING.resolve(fileName);

                DocumentMeta meta = new DocumentMeta(filePath);
                logger.info("Detected new file: " + meta);

                executor.submit(() -> processFile(meta));
            }

            key.reset();
        }
    }

    private static void processFile(DocumentMeta meta) {
        Path incomingFile = meta.getPath();

        try {
            // 1) Wait until file is fully written
            FileStabilizer.waitForCompleteWrite(incomingFile);
            logger.info("File stabilized: " + incomingFile.getFileName());

            // 2) Move to staging first (safety)
            Path staged = STAGING.resolve(incomingFile.getFileName());
            Files.move(incomingFile, staged,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

            logger.info("Moved to STAGING: " + staged.getFileName());

            // 3) Validate + Route
            Router.route(staged, VALID, INVALID, REJECTED);
            logger.info("Routed file: " + staged.getFileName());

            // 4) Archive if valid
            Path processedFile = VALID.resolve(staged.getFileName());
            if (Files.exists(processedFile)) {
                Archiver.archive(processedFile, ARCHIVE);
            }

        } catch (Exception e) {
            logger.severe("Error processing file: "
                    + incomingFile.getFileName() + " :: " + e.getMessage());
        }
    }

    private static void createDirectories() throws IOException {
        Files.createDirectories(INCOMING);
        Files.createDirectories(STAGING);
        Files.createDirectories(VALID);
        Files.createDirectories(INVALID);
        Files.createDirectories(REJECTED);
        Files.createDirectories(ARCHIVE);

        logger.info("Directory structure verified / created");
    }
}
