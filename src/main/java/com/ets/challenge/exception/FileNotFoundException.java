package com.ets.challenge.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(Long fileId) {
        super("File can not found with+ " + fileId + " id");
    }
}
