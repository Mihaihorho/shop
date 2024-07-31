package com.example.shop.database;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.model.Order.Status;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, OrderRepository orderRepository) {

        return args -> {
            log.info("Preloading " + productRepository.save(new Product("Milk", 5, 3)));
            log.info("Preloading " + productRepository.save(new Product("Bread", 1, 6)));
            log.info("Preloading " + orderRepository.save(new Order(1L, 1, Status.IN_PROGRESS)));
        };
    }
}