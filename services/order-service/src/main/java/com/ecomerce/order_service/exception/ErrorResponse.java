package com.ecomerce.order_service.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
