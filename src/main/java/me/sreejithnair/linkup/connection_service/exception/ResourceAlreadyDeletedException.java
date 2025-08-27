package me.sreejithnair.linkup.connection_service.exception;

public class ResourceAlreadyDeletedException extends RuntimeException {
    public ResourceAlreadyDeletedException(String message) {
        super(message);
    }
}
