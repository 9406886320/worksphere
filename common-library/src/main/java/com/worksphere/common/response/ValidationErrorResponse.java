package com.worksphere.common.response;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        List<String> messages,
        String path

) {
}