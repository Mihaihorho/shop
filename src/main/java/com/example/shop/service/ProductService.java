package com.example.shop.service;

import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        log.info("Adding product: {}", product);
        return productRepository.save(product);
    }

    public Product findProduct(Long id) {
        log.info("Finding product with Id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getAllProducts() {
        log.info("Finding all products");
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {
        log.info("Updating product with Id: {}", id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        return productRepository.save(existingProduct);
    }

    public Product updateProductName(Long id, String newName) {
        log.info("Updating the name of the product with Id: {}", id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(newName);
        return productRepository.save(existingProduct);
    }

    public Product updateProductPrice(Long id, double newPrice) {
        log.info("Updating the price of the product with Id: {}", id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        log.debug("Current price {} is going to be replaced by {}", existingProduct.getPrice(), newPrice);

        existingProduct.setPrice(newPrice);
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with Id: {}", id);
        productRepository.deleteById(id);
    }
}