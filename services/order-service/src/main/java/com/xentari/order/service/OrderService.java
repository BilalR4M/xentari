package com.xentari.order.service;

import com.xentari.order.entity.Order;
import com.xentari.order.entity.OrderItem;
import com.xentari.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(List<OrderItemData> itemDataList) {
        Order order = new Order();
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemData itemData : itemDataList) {
            OrderItem item = new OrderItem(itemData.productId(), itemData.quantity(), itemData.unitPrice());
            order.addItem(item);
            totalAmount = totalAmount.add(itemData.unitPrice().multiply(BigDecimal.valueOf(itemData.quantity())));
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    @Transactional
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public record OrderItemData(Long productId, Integer quantity, BigDecimal unitPrice) {}
}
