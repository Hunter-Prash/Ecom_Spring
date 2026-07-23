package com.example.Ecom.Services;

import com.example.Ecom.DTOS.InventoryRequest;
import com.example.Ecom.DTOS.InventoryResponse;
import com.example.Ecom.Entities.Inventory;
import com.example.Ecom.Exceptions.InsufficientStockException;
import com.example.Ecom.Exceptions.ResourceNotFoundException;
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

    public InventoryResponse receiveStock(Long productId,int quantity){
        try{
            Inventory item=inventoryRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given id not found"));
            item.setStockQuantity(item.getStockQuantity()+quantity);//update in-memory
            Inventory updatedItem = inventoryRepo.save(item);//save to DB
            return new InventoryResponse(updatedItem.getProductId(), updatedItem.getStockQuantity(), updatedItem.getLowStockThreshold(), updatedItem.getLocationRack());
        }catch(Exception ex){
            System.out.println("Service layer");
            System.out.println(ex.getMessage());
            throw ex;//propagate up to controller
        }
    }

    public InventoryResponse sellStock(Long productId, int quantity){
        try{
            Inventory item=inventoryRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given id not found"));
            if(item.getStockQuantity()>=quantity){
                item.setStockQuantity(item.getStockQuantity()-quantity);// updated in-memory
                if(item.getStockQuantity()<item.getLowStockThreshold()){
                    System.out.println("LOW-STOCK ALERT.");
                }
            }
            else {
                throw new InsufficientStockException("There is not enough stock left in the inventory to sell");
            }
            Inventory updatedItem = inventoryRepo.save(item);//save to DB
            return new InventoryResponse(updatedItem.getProductId(), updatedItem.getStockQuantity(), updatedItem.getLowStockThreshold(), updatedItem.getLocationRack());

        }catch(Exception ex){
            System.out.println("Service layer");
            System.out.println(ex.getMessage());
            throw ex;//propagate up to controller
        }

    }

    public InventoryResponse adjustStock(Long productId, int newQuantity){
        try{
            Inventory item=inventoryRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given id not found"));
            item.setStockQuantity(newQuantity);
            Inventory updatedItem=inventoryRepo.save(item);
            return new InventoryResponse(updatedItem.getProductId(), updatedItem.getStockQuantity(), updatedItem.getLowStockThreshold(), updatedItem.getLocationRack());
        } catch (Exception ex) {
            System.out.println("Service layer");
            System.out.println(ex.getMessage());
            throw ex;//propagate up to controller
        }
    }
}
