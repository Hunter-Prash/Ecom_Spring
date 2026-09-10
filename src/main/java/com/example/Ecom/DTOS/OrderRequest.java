package com.example.Ecom.DTOS;

import java.util.List;

/*
REQUEST JSON:

{
    "customerName": "Rahul",
    "productIds": [10, 25, 31],
    "quantities": [2, 1, 3]
}
*/

public record OrderRequest(
        String customerName,
        List<Long> productIds,
        List<Integer> quantities

) {
}