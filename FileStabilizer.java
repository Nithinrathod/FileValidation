package com.tcs.docwatcher.service;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileStabilizer {

    public static void waitForCompleteWrite(Path file) throws Exception {
        long size = -1;
        long newSize = Files.size(file);

        while (size != newSize) {
            size = newSize;
            Thread.sleep(300);
            newSize = Files.size(file);
        }
    }
}
