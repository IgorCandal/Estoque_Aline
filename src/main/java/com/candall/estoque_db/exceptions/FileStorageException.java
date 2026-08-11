package com.candall.estoque_db.exceptions;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message, Throwable causa){
        super(message, causa);
    }
}
