package com.cloud.ordertrackingapp.service;

import com.cloud.ordertrackingapp.api.OrderDTO;
import com.cloud.ordertrackingapp.api.Status;
import com.cloud.ordertrackingapp.dao.OrderDAO;
import com.cloud.ordertrackingapp.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Long createOrder(OrderDTO orderDTO) {
        log.info("creating a new order for userId {} and product name {}", orderDTO.getUserId(), orderDTO.getProductName());
        Order order = Order.builder()
                .userId(orderDTO.getUserId())
                .productName(orderDTO.getProductName())
                .count(orderDTO.getCount())
                .address(orderDTO.getAddress())
                .status(Status.PENDING.name())
                .build();

        Order savedOrder = orderDAO.save(order);
        log.info("finished creating order entity with order ID {}", savedOrder.getId());

        return savedOrder.getId();
    }

    public void updateOrderStatus(Long orderId, Status newStatus) {
        log.info("updating order with id {} to status {}", orderId, newStatus.name());
        Order order = orderDAO.findById(orderId);
        order.setStatus(newStatus.name());
        orderDAO.merge(order);
        log.info("finished updating order with id {} to status {}", orderId, newStatus.name());
    }

    public Optional<Order> getOrderStatus(Long orderId) {
        log.info("Fetching Order with id {}", orderId);
        Order order = orderDAO.findById(orderId);
        return Optional.of(order);
    }
}
