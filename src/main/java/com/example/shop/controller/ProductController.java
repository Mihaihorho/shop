package com.example.shop.controller;

import com.example.shop.assembler.ProductModelAssembler;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    ProductModelAssembler assembler;

    ProductController(ProductService productService, ProductModelAssembler assembler) {
        this.productService = productService;
        this.assembler = assembler;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITE')")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        EntityModel<Product> entityModel = assembler.toModel(productService.addProduct(product));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'READ', 'WRITE')")
    public EntityModel<Product> findProduct(@PathVariable("id") Long id) {
        Product product = productService.findProduct(id);
        return assembler.toModel(product);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'READ', 'WRITE')")
    public CollectionModel<EntityModel<Product>> getAllProducts() {
        List<EntityModel<Product>> products = productService.getAllProducts().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITE')")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        EntityModel<Product> entityModel = assembler.toModel(productService.updateProduct(id, product));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}/name")
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITE')")
    public ResponseEntity<?> updateProductName(@PathVariable("id") Long id, @RequestParam("newName") String newName) {
        EntityModel<Product> entityModel = assembler.toModel(productService.updateProductName(id, newName));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITE')")
    public ResponseEntity<?> updateProductPrice(@PathVariable("id") Long id, @RequestParam("newPrice") double newPrice) {
        EntityModel<Product> entityModel = assembler.toModel(productService.updateProductPrice(id, newPrice));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITE')")
    public ResponseEntity<?> updateProductStock(@PathVariable("id") Long id, @RequestParam("newStock") Integer newStock) {
        EntityModel<Product> entityModel = assembler.toModel(productService.updateProductStock(id, newStock));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}