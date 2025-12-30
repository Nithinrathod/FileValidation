package com.tcs.docwatcher.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class DocumentMeta {
    private final Path path;
    private final LocalDateTime detectedTime;

    public DocumentMeta(Path path) {
        this.path = path;
        this.detectedTime = LocalDateTime.now();
    }

    public Path getPath() {
        return path;
    }

    public LocalDateTime getDetectedTime() {
        return detectedTime;
    }

    @Override
    public String toString() {
        return "DocumentMeta{" +
                "path=" + path +
                ", detectedTime=" + detectedTime +
                '}';
    }
}
