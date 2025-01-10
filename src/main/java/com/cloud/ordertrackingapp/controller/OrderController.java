package com.cloud.ordertrackingapp.controller;

import com.cloud.ordertrackingapp.api.OrderDTO;
import com.cloud.ordertrackingapp.api.Status;
import com.cloud.ordertrackingapp.entity.Order;
import com.cloud.ordertrackingapp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/submit")
    public ResponseEntity<Long> submitOrder(OrderDTO orderDTO) {
        Long order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @PutMapping(value = "/{orderId}")
    public void changeOrderStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") Status status) {
        orderService.updateOrderStatus(orderId, status);
    }

    @GetMapping(value = "/{orderId}/status")
    public ResponseEntity<String> getOrderStatus(@PathVariable("orderId") Long orderId) {
        Optional<Order> orderEntity = orderService.getOrderStatus(orderId);
        return orderEntity.map(order -> ResponseEntity.ok(order.getStatus())).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
