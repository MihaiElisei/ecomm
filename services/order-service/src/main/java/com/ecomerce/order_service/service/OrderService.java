package com.ecomerce.order_service.service;

import com.ecomerce.order_service.customer.CustomerClient;
import com.ecomerce.order_service.exception.BusinessException;
import com.ecomerce.order_service.kafka.OrderConfirmation;
import com.ecomerce.order_service.kafka.OrderProducer;
import com.ecomerce.order_service.order.OrderRequest;
import com.ecomerce.order_service.order.OrderResponse;
import com.ecomerce.order_service.orderline.OrderLineRequest;
import com.ecomerce.order_service.orderline.OrderLineService;
import com.ecomerce.order_service.product.ProductClient;
import com.ecomerce.order_service.product.PurchaseRequest;
import com.ecomerce.order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer  orderProducer;

    public Integer createOrder(@Valid OrderRequest request) {
        // check the customer -> OpenFein
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided id: " + request.customerId()));

        //purchase the product -> product microservice (RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        //persist order
        var order = this.orderRepository.save(orderMapper.toOrder(request));

        //persist order lines
        for(PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                    null,
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
                    )
            );
        }

        //todo start payment process

        //send the order confirmation -> notification microservice (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find order with id: " + orderId));
    }
}
