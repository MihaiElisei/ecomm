package com.ecomerce.customer_service.services;

import com.ecomerce.customer_service.customer.Customer;
import com.ecomerce.customer_service.customer.CustomerRequest;
import com.ecomerce.customer_service.customer.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(@Valid CustomerRequest request) {

        if(request == null) return null;

        return Customer.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(request.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
