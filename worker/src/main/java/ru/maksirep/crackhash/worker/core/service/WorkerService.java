package ru.maksirep.crackhash.worker.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ru.maksirep.crackhash.worker.api.ApiPaths;
import ru.maksirep.crackhash.worker.api.dto.WorkerRequestDto;
import ru.maksirep.crackhash.worker.api.dto.WorkerResponseDto;
import ru.maksirep.crackhash.worker.core.exceptions.WorkerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;

@Service
public class WorkerService {

    private Hashtable<UUID, String> results = new Hashtable<>();

    private static final ArrayList<String> symbolList;

    static {
        symbolList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            String ch = "" + ((char) (i + 'a'));
            symbolList.add(ch);
            symbolList.add(ch.toUpperCase());
        }
        for (int i = 0; i < 10; i++) {
            symbolList.add(String.valueOf(i));
        }
    }

    public void crackHash(WorkerRequestDto workerRequestDto) {
        UUID uuid = workerRequestDto.requestId();
        setValue(uuid, "");
        bruteforceCrack(uuid,
                workerRequestDto.hash(),
                workerRequestDto.maxLength());
    }

    private void bruteforceCrack(UUID uuid, String hash, int maxLength) {
        ExecutorService service = Executors.newFixedThreadPool(maxLength);
        List<Future<Optional<List<String>>>> futureResults = new ArrayList<>();
        for (int i = 1; i < maxLength + 1; i++) {
            final int wordSize = i;
            Future<Optional<List<String>>> futureResult = service.submit(() -> Generator.permutation(symbolList)
                    .withRepetitions(wordSize)
                    .stream()
                    .filter(word -> checkHash(word, hash))
                    .findFirst());
            futureResults.add(futureResult);
        }
        for (Future<Optional<List<String>>> future : futureResults) {
            try {
                var result = future.get();
                if (result.isPresent()) {
                    setValue(uuid, String.join("", result.get()));
                    sendResultToManager(uuid);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new WorkerException("Ошибка получения результата вычислений");
            }
        }
        service.shutdown();
        if (getValue(uuid).isEmpty()) {
            throw new WorkerException("Некорректный хешкод");
        } else {
            dropValue(uuid);
        }
    }

    private void sendResultToManager(UUID uuid) {
        try {
            WorkerResponseDto workerResponseDto = new WorkerResponseDto(uuid, getValue(uuid));
            HttpClient client = HttpClient.newHttpClient();
            var objectMapper = new ObjectMapper();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiPaths.SEND_RESULT))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(workerResponseDto)))
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new WorkerException(ex.getMessage());
        }
    }

    private synchronized boolean checkResultExist(UUID uuid) {
        return results.get(uuid) != null;
    }

    private synchronized void setValue(UUID uuid, String data) {
        if (checkResultExist(uuid) && !results.get(uuid).isEmpty()) {
            throw new WorkerException(String.format("UUID \"%s\" уже существует", uuid));
        }
        results.put(uuid, data);
    }

    private synchronized String getValue(UUID uuid) {
        if (!checkResultExist(uuid)) {
            throw new WorkerException(String.format("UUID \"%s\" не найден", uuid));
        }
        return results.get(uuid);
    }

    private synchronized void dropValue(UUID uuid) {
        if (!checkResultExist(uuid)) {
            throw new WorkerException(String.format("UUID \"%s\" не найден", uuid));
        }
        results.remove(uuid);
    }

    private boolean checkHash(List<String> word, String hash) {
        String wordHash = DigestUtils.md5DigestAsHex(String.join("", word).getBytes());
        return wordHash.equals(hash);
    }
}
