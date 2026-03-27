package com.xentari.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    public Inventory() {}

    public Inventory(Long productId, Integer quantity, Integer reservedQuantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
    }

    public boolean canReserve(int requestedQuantity) {
        return (quantity - reservedQuantity) >= requestedQuantity;
    }

    public void reserve(int requestedQuantity) {
        this.reservedQuantity += requestedQuantity;
    }

    public void releaseReservation(int quantity) {
        this.reservedQuantity = Math.max(0, this.reservedQuantity - quantity);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }
}
