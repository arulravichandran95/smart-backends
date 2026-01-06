package com.smart.procurement1.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.smart.procurement1.model.Product;
import com.smart.procurement1.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }
}

