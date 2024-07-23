package ru.maksirep.crackhash.manager.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.maksirep.crackhash.manager.api.validation.ValidationMessage;

import java.util.UUID;

public record WorkerResponseDto(

        @NotNull
        UUID requestId,

        @NotNull
        @Size(min = 1, max = 4, message = ValidationMessage.DATA_SIZE)
        String data) {
}
