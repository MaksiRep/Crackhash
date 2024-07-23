package ru.maksirep.crackhash.worker.api.dto;

import jakarta.validation.constraints.*;
import ru.maksirep.crackhash.worker.api.validation.ValidationMessage;
import ru.maksirep.crackhash.worker.api.validation.ValidationRegex;

import java.util.UUID;

public record WorkerRequestDto(

        @NotNull
        UUID requestId,

        @NotNull
        @Size(min = 32, max = 32, message = ValidationMessage.HASH_SIZE)
        @Pattern(regexp = ValidationRegex.HASH_REGEX, message = ValidationMessage.HASH_REGEX)
        String hash,

        @NotNull
        @Min(value = 1, message = ValidationMessage.MIN_WORD_LENGTH)
        @Max(value = 4, message = ValidationMessage.MAX_WORD_LENGTH)
        int maxLength) {
}
