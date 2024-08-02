package com.example.shop.service;

import com.example.shop.exception.InsufficientStockException;
import com.example.shop.exception.OrderNotFoundException;
import com.example.shop.exception.OrderNotInProgressException;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.model.Order;
import com.example.shop.model.Order.Status;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    public Order addOrder(Order order) {
        log.info("Adding order: {}", order);
        Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new ProductNotFoundException(order.getProductId()));
        if (product.getStock() < order.getQuantity()) {
            log.debug("Product available stock is: {}, while the requested order quantity is: {}", product.getStock(), order.getQuantity());
            throw new InsufficientStockException();
        }

        product.setStock(product.getStock() - order.getQuantity());
        productRepository.save(product);

        order.setStatus(Status.IN_PROGRESS);
        return orderRepository.save(order);
    }

    public Order findOrder(Long id) {
        log.info("Finding order with Id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<Order> getAllOrders() {
        log.info("Finding all orders");
        return orderRepository.findAll();
    }

    public Order completeOrder(Long id) {
        log.info("Completing order with Id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (Status.IN_PROGRESS != order.getStatus()) throw new OrderNotInProgressException(id);
        order.setStatus(Status.COMPLETED);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order with Id: {}", id);
        orderRepository.deleteById(id);
    }

    public Order cancelOrder(Long id) {
        log.info("Cancelling order with Id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (Status.IN_PROGRESS != order.getStatus()) throw new OrderNotInProgressException(id);
        Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new ProductNotFoundException(order.getProductId()));

        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);
        order.setStatus(Status.CANCELLED);
        return orderRepository.save(order);
    }
}