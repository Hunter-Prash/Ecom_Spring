package com.example.Ecom.DTOS;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;


/*
RESPONSE JSON:

{
    "orderId": 101,
    "customerName": "Rahul",
    "status": "PLACED",
    "items": [
        {
            "productId": 10,
            "quantity": 2,
            "priceAtPurchase": 60000.00
        },
        {
            "productId": 25,
            "quantity": 1,
            "priceAtPurchase": 8000.00
        },
        {
            "productId": 31,
            "quantity": 3,
            "priceAtPurchase": 500.00
        }
    ],
    "createdAt": "2026-09-11T00:30:00+05:30"
}
*/
public record OrderResponse(
        Long orderId,
        String customerName,
        String status,
        List<Item> items,
        ZonedDateTime createdAt
) {

    public record Item(
            Long productId,
            Integer quantity,
            BigDecimal priceAtPurchase
    ) {}
}