package com.smart.procurement1.controller;

import com.smart.procurement1.model.Product;
import com.smart.procurement1.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // Vendor uploads product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    // Admin views all products
    @GetMapping
    public List<Product> getProducts() {
        return service.getAllProducts();
    }
}
