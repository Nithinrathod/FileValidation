package com.tcs.docwatcher.service;

import com.tcs.docwatcher.model.DocStatus;

public class Validator {
    public static DocStatus classify(String fileName) {

        String lower = fileName.toLowerCase();

        if (lower.contains("valid")) return DocStatus.VALID;
        if (lower.contains("corrupt")) return DocStatus.INVALID;

        return DocStatus.REJECTED;
    }

    public static boolean supportedFormat(String fileName) {
        return fileName.endsWith(".pdf")
                || fileName.endsWith(".xml")
                || fileName.endsWith(".txt")
                || fileName.endsWith(".csv")
                || fileName.endsWith(".json");
    }
}
