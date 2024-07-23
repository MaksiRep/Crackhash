package ru.maksirep.crackhash.manager.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.maksirep.crackhash.manager.api.ApiPaths;
import ru.maksirep.crackhash.manager.api.dto.*;
import ru.maksirep.crackhash.manager.core.enums.ResultStatus;
import ru.maksirep.crackhash.manager.core.exceptions.ManagerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Hashtable;
import java.util.UUID;

@Service
public class ManagerService {

    static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    private final Hashtable<UUID, ResultResponse> results = new Hashtable<>();

    public CrackHashResponse crackHash(CrackHashDto crackHashDto) {
        UUID uuid = UUID.randomUUID();
        WorkerRequestDto workerRequestDto = new WorkerRequestDto(uuid,
                crackHashDto.hash(),
                crackHashDto.maxLength());
        try {
            results.put(uuid, new ResultResponse(ResultStatus.IN_PROGRESS, null));
            sendHashToWorker(workerRequestDto);
            return new CrackHashResponse(uuid);
        } catch (Exception ex) {
            throw new ManagerException(ex.getMessage());
        }
    }

    public ResultResponse getStatus(UUID requestId) {
        checkResultExist(requestId);
        return results.get(requestId);
    }

    public void setResult(WorkerResponseDto workerResponseDto) {
        setResult(workerResponseDto.requestId(),
                workerResponseDto.data(),
                ResultStatus.READY);
    }

    private void sendHashToWorker(WorkerRequestDto body) throws JsonProcessingException {
        HttpClient client = HttpClient.newHttpClient();
        var objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiPaths.WORKER_CRACK_PATH))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    int status = response.statusCode();
                    if (status != 200) {
                        try {
                            var errorMapper = new ObjectMapper();
                            ManagerException ex = errorMapper.readValue(response.body(), ManagerException.class);
                            setResult(body.requestId(), ex.getMessage(), ResultStatus.ERROR);
                            logger.error(ex.getMessage());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .exceptionally(throwable -> {
                    setResult(body.requestId(), "Воркер не активен", ResultStatus.ERROR);
                    logger.error("Воркер не активен");
                    return null;
                });
    }

    private void checkResultExist(UUID uuid) {
        ResultResponse response = results.get(uuid);
        if (response == null) {
            throw new ManagerException(String.format("Не существует UUID со значением \"%s\"", uuid));
        }
    }

    private void setResult(UUID uuid, String data, ResultStatus status) {
        checkResultExist(uuid);
        if (data != null) {
            results.put(uuid, new ResultResponse(status, data));
        }
    }
}
