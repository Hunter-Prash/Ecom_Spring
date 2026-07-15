package com.example.Ecom.Services;

import com.example.Ecom.DTOS.ProductCreateRequest;
import com.example.Ecom.DTOS.ProductResponse;
import com.example.Ecom.Entities.Inventory;
import com.example.Ecom.Entities.Product;
import com.example.Ecom.Exceptions.InsufficientStockException;
import com.example.Ecom.Exceptions.ResourceNotFoundException;
import com.example.Ecom.Repositories.InventoryRepo;
import com.example.Ecom.Repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;

    public ProductService(ProductRepo productRepo, InventoryRepo inventoryRepo) {
        this.productRepo = productRepo;
        this.inventoryRepo =inventoryRepo;
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        try {

            if (productRepo.findBySku(request.sku()).isPresent()) {
                throw new IllegalArgumentException("Product with " + request.sku() + " already exists");
            }

            Product product = Product.builder()
                    .sku(request.sku())
                    .name(request.name())
                    .description(request.description())
                    .price(request.price())
                    .isActive(true)
                    .build();

            productRepo.save(product);

            Inventory inventory = Inventory.builder()
                    .product(product)
                    .stockQuantity(request.initialStock())
                    .lowStockThreshold(request.lowStockThreshold())
                    .locationRack("TEMP-RACK-01")
                    .build();

            inventoryRepo.save(inventory);

            return new ProductResponse(
                    product.getId(),
                    product.getSku(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    inventory.getStockQuantity(),
                    inventory.getLocationRack()
            );

        } catch (IllegalArgumentException ex) {

            System.out.println("Duplicate Product Error");
            System.out.println(ex.getMessage());

            return null;
        }
    }

    @Transactional
    public ProductResponse getProductById(Long id) {

        try {

            Product product = productRepo.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with ID: " + id));

            Inventory inventory = inventoryRepo.findById(product.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inventory not found for Product ID: " + id));

            return new ProductResponse(
                    product.getId(),
                    product.getSku(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    inventory.getStockQuantity(),
                    inventory.getLocationRack()
            );

        } catch (ResourceNotFoundException ex) {

            System.out.println("Resource Not Found");
            System.out.println(ex.getMessage());

            return null;
        }
    }

    public List<ProductResponse> getAllProducts() {

        /*
         * -------------------- OLD WAY (Without Streams) --------------------
         *
         * List<Product> products = productRepo.findAll();
         *
         * List<ProductResponse> responses = new ArrayList<>();
         *
         * for (Product product : products) {
         *
         *     Inventory inventory = inventoryRepo.findById(product.getId())
         *             .orElse(null);
         *
         *     ProductResponse response = new ProductResponse(
         *             product.getId(),
         *             product.getSku(),
         *             product.getName(),
         *             product.getDescription(),
         *             product.getPrice(),
         *             inventory != null ? inventory.getStockQuantity() : 0,
         *             inventory != null ? inventory.getLocationRack() : null
         *     );
         *
         *     responses.add(response);
         * }
         *
         * return responses;
         *
         * ------------------------------------------------------------------
         */

        // Stream version (does exactly the same thing as above)
        return productRepo.findAll()
                .stream()
                .map(product -> {

                    Inventory inventory = inventoryRepo.findById(product.getId())
                            .orElse(null);

                    return new ProductResponse(
                            product.getId(),
                            product.getSku(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            inventory != null ? inventory.getStockQuantity() : 0,
                            inventory != null ? inventory.getLocationRack() : null
                    );

                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse updateStock(Long productId, int quantityToChange) {

        try {

            Inventory inventory = inventoryRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inventory record not found for Product ID: " + productId));

            int newQuantity = inventory.getStockQuantity() + quantityToChange;

            if (newQuantity < 0) {
                throw new InsufficientStockException(
                        "Cannot reduce stock below zero. Current stock: "
                                + inventory.getStockQuantity());
            }

            inventory.setStockQuantity(newQuantity);

            inventoryRepo.save(inventory);

            Product product = inventory.getProduct();

            return new ProductResponse(
                    product.getId(),
                    product.getSku(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    inventory.getStockQuantity(),
                    inventory.getLocationRack()
            );

        } catch (ResourceNotFoundException ex) {
            //catch block in the service layer is useful when This is useful when you want to do something in the service (like logging) but still let the controller handle the exception.
            System.out.println("Inventory Not Found");
            System.out.println(ex.getMessage());

            throw ex;//send it to the controller

        } catch (InsufficientStockException ex) {

            System.out.println("Stock Error");
            System.out.println(ex.getMessage());

            throw ex;//sending to controller
        }
    }
}