package com.example.shop.controller;

import com.example.shop.model.Order;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        try {
            return orderService.addOrder(order);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Order findOrder(@PathVariable("id") Long id) {
        return orderService.findOrder(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}/complete")
    public Order completeOrder(@PathVariable("id") Long id) {
        try {
            return orderService.completeOrder(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }

    @DeleteMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable("id") Long id) {
        try {
            return orderService.cancelOrder(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}