package com.example.Ecom.DTOS;

import java.math.BigDecimal;

/**
 * The clean, flattened JSON structure we send back to the client.
 * Notice how it combines Product details AND Inventory stock into one clean response.
 */
public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        int currentStock,
        String locationRack
) {}

//since this is a record JAVA automatically generates
/*
*
* public String name(){
    return this.name;
}

public int age(){
    return this.age;
}
*
*
*so when we usew this in our serveice we write request.name() and req.sku() */

