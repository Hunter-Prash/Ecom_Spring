package com.example.Ecom.Services;

import com.example.Ecom.Repositories.InventoryRepo;
import com.example.Ecom.Repositories.ProductRepo;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;

    public  InventoryService(InventoryRepo inventoryRepo, ProductRepo productRepo){
        this.inventoryRepo=inventoryRepo;
        this.productRepo=productRepo;

    }


}
