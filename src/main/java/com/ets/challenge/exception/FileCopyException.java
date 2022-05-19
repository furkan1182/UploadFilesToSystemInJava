package com.ets.challenge.exception;

public class FileCopyException extends RuntimeException {
    public FileCopyException(String fileName) {
        super("An unexpected error occurred while copying the " + fileName + " file.");
    }
}
