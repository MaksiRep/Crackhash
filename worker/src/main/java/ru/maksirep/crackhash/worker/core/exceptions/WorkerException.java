package ru.maksirep.crackhash.worker.core.exceptions;

public class WorkerException extends RuntimeException {

    String message;

    public WorkerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
