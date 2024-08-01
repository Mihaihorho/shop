package com.example.shop.assembler;

import com.example.shop.controller.ProductController;
import com.example.shop.model.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {
    @Override
    public EntityModel<Product> toModel(Product product) {

        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).findProduct(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
    }
}
