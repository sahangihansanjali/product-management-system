package com.sahangi.productmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long productId,
            @RequestParam int quantity) {

        Order order = orderService.createOrder(
                productId,
                quantity
        );

        return ResponseEntity.ok(order);
    }

    // Confirm an existing order
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(
            @PathVariable Long id) {

        Order order = orderService.confirmOrder(id);

        return ResponseEntity.ok(order);
    }
}