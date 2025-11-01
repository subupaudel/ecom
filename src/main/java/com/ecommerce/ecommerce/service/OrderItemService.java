package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.OrderRequest;
import com.ecommerce.ecommerce.dto.Response;
import com.ecommerce.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId, String status);
    Response filterOrderItem(OrderStatus status, LocalDateTime startDate,  LocalDateTime endDate, Long itemId, Pageable pageable);
}
