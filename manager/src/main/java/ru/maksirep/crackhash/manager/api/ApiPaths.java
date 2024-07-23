package ru.maksirep.crackhash.manager.api;

public class ApiPaths {

    private ApiPaths() {
    }

    public static final String CRACK = "/api/hash/crack";

    public static final String STATUS = "/api/hash/status";

    public static final String SET_RESULT = "/internal/api/manager/hash/crack/request";

    public static final String WORKER_CRACK_PATH = "http://worker:8083/internal/api/worker/hash/crack/task";
}
