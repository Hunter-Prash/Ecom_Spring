package com.example.Ecom.Controllers;


import com.example.Ecom.DTOS.ProductCreateRequest;
import com.example.Ecom.DTOS.ProductResponse;
import com.example.Ecom.Exceptions.InsufficientStockException;
import com.example.Ecom.Exceptions.ResourceNotFoundException;
import com.example.Ecom.Services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @GetMapping("/getProducts")
    public List<ProductResponse> getAllProducts(){
        try{return productService.getAllProducts();}
        catch (ResourceNotFoundException | InsufficientStockException ex){
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }

    }

    @PostMapping("/createProduct")
    public ProductResponse createProduct(@RequestBody ProductCreateRequest req){
        try{return productService.createProduct(req);}
        catch (IllegalArgumentException ex){
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse updateStock(@PathVariable Long id,@RequestParam int quantityToChange){
        try{return productService.updateStock(id,quantityToChange);}
        catch (ResourceNotFoundException | InsufficientStockException ex){
            System.out.println("Controller Layer:");
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
