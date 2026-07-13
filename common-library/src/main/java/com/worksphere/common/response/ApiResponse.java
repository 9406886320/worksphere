package com.worksphere.common.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(boolean success,
                             String message, LocalDateTime timestamp,
                             T data) {
}
