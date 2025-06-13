package com.ecomerce.payment_service.controller;

import com.ecomerce.payment_service.customer.Customer;
import com.ecomerce.payment_service.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}
