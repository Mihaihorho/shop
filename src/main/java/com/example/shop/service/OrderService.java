package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.Order.Status;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    public Order addOrder(Order order) throws Exception {
        Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new Exception("Product not found"));
        if (product.getStock() < order.getQuantity()) {
            throw new Exception("Insufficient stock");
        }

        product.setStock(product.getStock() - order.getQuantity());
        productRepository.save(product);

        order.setStatus(Status.IN_PROGRESS);
        return orderRepository.save(order);
    }

    public Order findOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order completeOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
        if (Status.IN_PROGRESS != order.getStatus())
            throw new Exception("Order must be in progress");
        order.setStatus(Status.COMPLETED);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order cancelOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
        if (Status.IN_PROGRESS != order.getStatus())
            throw new Exception("Order must be in progress");
        Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new Exception("Product not found"));

        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);
        order.setStatus(Status.CANCELLED);
        return orderRepository.save(order);
    }
}