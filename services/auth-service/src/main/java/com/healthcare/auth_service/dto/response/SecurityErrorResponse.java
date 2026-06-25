package com.healthcare.auth_service.dto.response;

import java.time.OffsetDateTime;

public record SecurityErrorResponse(

        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
