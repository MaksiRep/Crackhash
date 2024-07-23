package ru.maksirep.crackhash.manager.api.dto;

import ru.maksirep.crackhash.manager.core.enums.ResultStatus;

public record ResultResponse(
        ResultStatus status,
        String data) {
}
