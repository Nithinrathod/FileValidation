package com.tcs.docwatcher.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.tcs.docwatcher.model.DocStatus;

public class Router {

    public static void route(Path file,
                             Path validDir,
                             Path invalidDir,
                             Path rejectedDir) throws Exception {

        DocStatus status = Validator.classify(file.getFileName().toString());

        switch (status) {
            case VALID -> move(file, validDir);
            case INVALID -> move(file, invalidDir);
            default -> move(file, rejectedDir);
        }
    }

    private static void move(Path src, Path target) throws Exception {
        Files.move(src,
                target.resolve(src.getFileName()),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
}
