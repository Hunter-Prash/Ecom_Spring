package com.example.Ecom.Controllers;

import com.example.Ecom.DTOS.InventoryRequest;
import com.example.Ecom.DTOS.InventoryResponse;
import com.example.Ecom.Services.InventoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{id}/receiveStock")
    public InventoryResponse receiveStock(@PathVariable Long id, @RequestBody InventoryRequest request) {
        try {
            return inventoryService.receiveStock(id, request.quantity());

        } catch (Exception ex) {
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @PostMapping("/{id}/sell")
    public InventoryResponse sellStock(@PathVariable Long id, @RequestBody InventoryRequest req) {
        try {
            return inventoryService.sellStock(id, req.quantity());

        } catch (Exception ex) {
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @PatchMapping("/{id}/adjust")
    public InventoryResponse adjustStock(@PathVariable Long id, @RequestBody InventoryRequest req) {
        try {
            return inventoryService.adjustStock(id, req.quantity());

        } catch (Exception ex) {
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
