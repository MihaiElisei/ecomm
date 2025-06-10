package com.ecomerce.customer_service.exceptions;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
