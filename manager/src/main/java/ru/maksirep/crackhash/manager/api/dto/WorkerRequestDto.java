package ru.maksirep.crackhash.manager.api.dto;

import java.util.UUID;

public record WorkerRequestDto(
        UUID requestId,
        String hash,
        int maxLength) {
}
