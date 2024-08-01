package com.example.shop.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("The stock is not big enough to sustain the order!");
    }
}
