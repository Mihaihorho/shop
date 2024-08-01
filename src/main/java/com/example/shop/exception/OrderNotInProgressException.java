package com.example.shop.exception;

public class OrderNotInProgressException extends RuntimeException {
    public OrderNotInProgressException(Long id) {
        super("Order " + id + " must have status 'IN_PROGRESS'");
    }
}
