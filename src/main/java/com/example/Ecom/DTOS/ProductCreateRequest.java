package com.example.Ecom.DTOS;

import java.math.BigDecimal;

/**
 * The exact JSON structure we expect from the frontend or Postman
 * when creating a new product.
 */
public record ProductCreateRequest(
        String sku,
        String name,
        String description,
        BigDecimal price,
        int initialStock,
        int lowStockThreshold
) {}
