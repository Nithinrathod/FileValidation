package com.tcs.docwatcher.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Archiver {

    public static void archive(Path file, Path archiveRoot) throws Exception {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        Path folder = archiveRoot.resolve(date);
        Files.createDirectories(folder);

        Files.move(file,
                folder.resolve(ts + "_" + file.getFileName()),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
}
