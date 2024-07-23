package ru.maksirep.crackhash.manager.api.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.maksirep.crackhash.manager.api.ApiPaths;
import ru.maksirep.crackhash.manager.api.dto.CrackHashDto;
import ru.maksirep.crackhash.manager.api.dto.CrackHashResponse;
import ru.maksirep.crackhash.manager.api.dto.ResultResponse;
import ru.maksirep.crackhash.manager.api.dto.WorkerResponseDto;
import ru.maksirep.crackhash.manager.core.service.ManagerService;

import java.util.UUID;

@RestController
@Validated
public class ManagerController {

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    private final ManagerService managerService;

    @PostMapping(ApiPaths.CRACK)
    CrackHashResponse crackHash(@Valid @RequestBody CrackHashDto crackHashDto) {
        return managerService.crackHash(crackHashDto);
    }

    @GetMapping(ApiPaths.STATUS)
    ResultResponse getStatus(@RequestParam UUID requestId) {
        return managerService.getStatus(requestId);
    }

    @PatchMapping(ApiPaths.SET_RESULT)
    void setResult(@RequestBody WorkerResponseDto workerResponseDto) {
        managerService.setResult(workerResponseDto);
    }
}
