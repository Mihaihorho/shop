package com.example.shop.database;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.model.Order.Status;
import com.example.shop.model.User;
import com.example.shop.model.User.Role;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, OrderRepository orderRepository,
                                   UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            log.info("Preloading " + productRepository.save(new Product("Milk", 5, 3)));
            log.info("Preloading " + productRepository.save(new Product("Bread", 1, 6)));
            log.info("Preloading " + orderRepository.save(new Order(1L, 1, Status.IN_PROGRESS)));
            log.info("Preloading " + userRepository.save(new User("admin", passwordEncoder.encode("admin"), Role.ADMIN)));
        };
    }
}