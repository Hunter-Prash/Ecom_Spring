package com.example.Ecom.Services;


import com.example.Ecom.DTOS.OrderRequest;
import com.example.Ecom.DTOS.OrderResponse;
import com.example.Ecom.Entities.Inventory;
import com.example.Ecom.Entities.Order;
import com.example.Ecom.Entities.OrderItem;
import com.example.Ecom.Entities.Product;
import com.example.Ecom.Exceptions.InsufficientStockException;
import com.example.Ecom.Exceptions.ResourceNotFoundException;
import com.example.Ecom.Repositories.InventoryRepo;
import com.example.Ecom.Repositories.OrderItemRepo;
import com.example.Ecom.Repositories.OrderRepo;
import com.example.Ecom.Repositories.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Builder
public class OrderService {
    private final OrderRepo orderRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;

    //DEPENDENCY INJECTION
    public OrderService(OrderRepo orderRepo,InventoryRepo inventoryRepo,OrderItemRepo orderItemRepo, ProductRepo productRepo){
        this.inventoryRepo=inventoryRepo;
        this.orderRepo=orderRepo;
        this.orderItemRepo=orderItemRepo;
        this.productRepo=productRepo;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest req){

        if (req.productIds().size() != req.quantities().size()) {
            throw new IllegalArgumentException(
                    "Product IDs and quantities must have the same size"
            );
        }
        Order order=Order.builder()
                .customerName(req.customerName())
                .status("PLACED")
                .build();

        orderRepo.save(order);

        for(int i=0;i<req.productIds().size();i++){
            Long productId=req.productIds().get(i);
            Integer quantity=req.quantities().get(i);

           Product product= productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
           Inventory inventory=inventoryRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Inventory not found"));

            if (inventory.getStockQuantity() < quantity) {
                throw new InsufficientStockException("Insufficient stock");
            }
            inventory.setStockQuantity(
                    inventory.getStockQuantity() - quantity
            );

            inventoryRepo.save(inventory);

            //Create OrderItem
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItemRepo.save(item);
            
            // Add the item to the order in memory so it appears in the JSON response
            order.getOrderItems().add(item);

        }
        return new OrderResponse(order.getOrderId(), order.getCustomerName(),order.getStatus(),order.getOrderItems(),order.getCreatedAt());
    }
}
