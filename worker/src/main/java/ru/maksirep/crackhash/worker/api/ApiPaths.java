package ru.maksirep.crackhash.worker.api;

public class ApiPaths {

    private ApiPaths() {
    }

    public static final String CRACK = "/internal/api/worker/hash/crack/task";

    public static final String SEND_RESULT = "http://manager:8082/internal/api/manager/hash/crack/request";
}
