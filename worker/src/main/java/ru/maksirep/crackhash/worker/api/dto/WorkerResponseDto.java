package ru.maksirep.crackhash.worker.api.dto;

import java.util.UUID;

public record WorkerResponseDto(
        UUID requestId,
        String data
) {
}
