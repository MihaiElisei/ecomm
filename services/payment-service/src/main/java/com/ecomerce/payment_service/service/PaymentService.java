package com.ecomerce.payment_service.service;

import com.ecomerce.payment_service.controller.PaymentRequest;
import com.ecomerce.payment_service.notification.NotificationProducer;
import com.ecomerce.payment_service.notification.PaymentNotificationRequest;
import com.ecomerce.payment_service.payment.PaymentMapper;
import com.ecomerce.payment_service.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(@Valid PaymentRequest request) {
        var payment = paymentRepository.save(paymentMapper.toPayment(request));
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstName(),
                        request.customer().lastName(),
                        request.customer().email()
                )
        );
        return payment.getId();
    }
}
