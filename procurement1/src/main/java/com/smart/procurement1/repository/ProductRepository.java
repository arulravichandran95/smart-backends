package com.smart.procurement1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.procurement1.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

