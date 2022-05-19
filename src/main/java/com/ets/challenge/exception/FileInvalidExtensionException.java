package com.ets.challenge.exception;

public class FileInvalidExtensionException extends RuntimeException {
    public FileInvalidExtensionException(String invalidExtension) {
        super("Can not upload with type " + invalidExtension);
    }
}
