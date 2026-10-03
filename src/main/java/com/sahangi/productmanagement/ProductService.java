package com.sahangi.productmanagement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name) {

        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product getProductById(Long id) {

        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    // Total number of products
    public long getTotalProducts() {

        return productRepository.count();
    }

    // Total quantity of all products
    public int getTotalQuantity() {

        return productRepository.findAll()
                .stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    // Total inventory value
    public double getTotalInventoryValue() {

        return productRepository.findAll()
                .stream()
                .mapToDouble(product ->
                        product.getPrice() * product.getQuantity())
                .sum();
    }

    // Transactional stock reduction workflow
    @Transactional
    public Product reduceStock(Long id, int quantity) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock"
            );
        }

        product.setQuantity(
                product.getQuantity() - quantity
        );

        return productRepository.save(product);
    }
}