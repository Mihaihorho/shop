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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(100);

        order = new Order();
        order.setId(1L);
        order.setProductId(product.getId());
        order.setQuantity(10);
        order.setStatus(Status.IN_PROGRESS);
    }

    @Test
    public void testAddOrder_success() {
        when(productRepository.findById(order.getProductId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.addOrder(order);
        assertNotNull(createdOrder);
        assertEquals(order.getId(), createdOrder.getId());
        assertEquals(Status.IN_PROGRESS, createdOrder.getStatus());

        verify(productRepository, times(1)).findById(order.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testAddOrder_productNotFound() {
        when(productRepository.findById(order.getProductId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            orderService.addOrder(order);
        });

        verify(productRepository, times(1)).findById(order.getProductId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testAddOrder_insufficientStock() {
        product.setStock(5);
        when(productRepository.findById(order.getProductId())).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> {
            orderService.addOrder(order);
        });

        verify(productRepository, times(1)).findById(order.getProductId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testFindOrder() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Order foundOrder = orderService.findOrder(order.getId());
        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    public void testFindOrderNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = Arrays.asList(order, new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> allOrders = orderService.getAllOrders();
        assertNotNull(allOrders);
        assertEquals(2, allOrders.size());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testCompleteOrder_success() {
        order.setStatus(Status.IN_PROGRESS);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order completedOrder = orderService.completeOrder(order.getId());
        assertNotNull(completedOrder);
        assertEquals(Status.COMPLETED, completedOrder.getStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCompleteOrder_orderNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.completeOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testCompleteOrder_invalidStatus() {
        order.setStatus(Status.CANCELLED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(OrderNotInProgressException.class, () -> {
            orderService.completeOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testDeleteOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(order.getId());

        orderService.deleteOrder(order.getId());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).deleteById(order.getId());
    }

    @Test
    public void testDeleteOrderNotFound() {
        doNothing().when(orderRepository).deleteById(order.getId());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    public void testCancelOrder_success() {
        order.setStatus(Status.IN_PROGRESS);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(order.getProductId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Order cancelledOrder = orderService.cancelOrder(order.getId());
        assertNotNull(cancelledOrder);
        assertEquals(Status.CANCELLED, cancelledOrder.getStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(productRepository, times(1)).findById(order.getProductId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testCancelOrder_orderNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(productRepository, times(0)).findById(order.getProductId());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void testCancelOrder_invalidStatus() {
        order.setStatus(Status.COMPLETED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(OrderNotInProgressException.class, () -> {
            orderService.cancelOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(productRepository, times(0)).findById(order.getProductId());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void testCancelOrder_productNotFound() {
        order.setProductId(2L);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(ProductNotFoundException.class, () -> {
            orderService.cancelOrder(order.getId());
        });

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(productRepository, times(1)).findById(order.getProductId());
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
