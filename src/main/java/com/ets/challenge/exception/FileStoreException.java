package com.ets.challenge.exception;

public class FileStoreException extends RuntimeException {
    public FileStoreException(String location) {
        super("Could not create the directory where the uploaded files will be stored. Location: " + location);
    }
}
