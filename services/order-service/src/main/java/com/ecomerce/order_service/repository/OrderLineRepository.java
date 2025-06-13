package com.ecomerce.order_service.repository;

import com.ecomerce.order_service.orderline.OrderLine;
import com.ecomerce.order_service.orderline.OrderLineResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(Integer orderId);
}
