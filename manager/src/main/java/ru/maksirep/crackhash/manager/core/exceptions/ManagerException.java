package ru.maksirep.crackhash.manager.core.exceptions;

public class ManagerException extends RuntimeException {

    private final String message;

    public ManagerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
