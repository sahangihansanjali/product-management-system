package com.sahangi.productmanagement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // Create a new order
    public Order createOrder(Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    // Confirm order and reduce stock
    @Transactional
    public Order confirmOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new OrderNotFoundException(
                    "Order not found: " + orderId);
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException(
                    "Order is already processed");
        }

        Product product = order.getProduct();

        if (product.getQuantity() < order.getQuantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: "
                            + product.getName());
        }

        product.setQuantity(
                product.getQuantity() - order.getQuantity()
        );

        productRepository.save(product);

        order.setStatus("CONFIRMED");

        return orderRepository.save(order);
    }
}