package com.example.Ecom.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_name",nullable = false)
    private String customerName;

    @Column(name = "status",nullable = false)
    private String status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems=new ArrayList<>();

    @Column(name = "created_at",insertable = false,updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at",insertable = false,updatable = false)
    private ZonedDateTime updatedAt;
}
