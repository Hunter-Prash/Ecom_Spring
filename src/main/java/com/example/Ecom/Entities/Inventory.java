package com.example.Ecom.Entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@Builder
public class Inventory {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY)// Tells Java that one row in the inventory table links to exactly one row in the product table.
    @MapsId//Take the primary key from the Product class and copy it directly into the @Id field (productId) of this Inventory class
    @JoinColumn(name = "product_id")//Tells Java that the inventory table has a column literally named product_id. This column acts as the bridge. It holds the foreign key that points back to the product table.
    private Product product;//this is a product object from the products table


    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 5;

    @Column(name = "location_rack", length = 50)
    private String locationRack;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private ZonedDateTime updatedAt;
}
