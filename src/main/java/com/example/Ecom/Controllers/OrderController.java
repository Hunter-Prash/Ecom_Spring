package com.example.Ecom.Controllers;

import com.example.Ecom.DTOS.OrderRequest;
import com.example.Ecom.DTOS.OrderResponse;
import com.example.Ecom.Services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest req) {
        return new ResponseEntity<>(orderService.createOrder(req), HttpStatus.CREATED);
    }
}
