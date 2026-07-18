package com.example.Ecom.DTOS;

public record InventoryResponse(

        Long productId,

        Integer stockQuantity,

        Integer lowStockThreshold,

        String locationRack

) {
}