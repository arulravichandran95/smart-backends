package com.smart.procurement1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private double price;
    private String vendorName;

    // REQUIRED by JPA
    public Product() {
    }

    public Product(Long id, String name, String category, double price, String vendorName) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.vendorName = vendorName;
    }
}
