package ru.maksirep.crackhash.worker.api.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maksirep.crackhash.worker.api.ApiPaths;
import ru.maksirep.crackhash.worker.api.dto.WorkerRequestDto;
import ru.maksirep.crackhash.worker.core.service.WorkerService;

@RestController
@Validated
public class WorkerController {

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    private final WorkerService workerService;

    @PostMapping(ApiPaths.CRACK)
    void crackHash(@Valid @RequestBody WorkerRequestDto workerRequestDto) {
        workerService.crackHash(workerRequestDto);
    }
}
