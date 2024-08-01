package com.example.shop.controller;

import com.example.shop.assembler.OrderModelAssembler;
import com.example.shop.model.Order;
import com.example.shop.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;

    OrderModelAssembler assembler;

    OrderController(OrderService orderService, OrderModelAssembler assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        EntityModel<Order> entityModel = assembler.toModel(orderService.addOrder(order));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<Order> findOrder(@PathVariable("id") Long id) {
        Order order = orderService.findOrder(id);
        return assembler.toModel(order);
    }

    @GetMapping
    public CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orders = orderService.getAllOrders().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable("id") Long id) {
        EntityModel<Order> entityModel = assembler.toModel(orderService.completeOrder(id));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) {
        EntityModel<Order> entityModel = assembler.toModel(orderService.cancelOrder(id));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}