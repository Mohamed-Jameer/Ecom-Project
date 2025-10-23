package com.example.Ecom_Project.dto;

import com.example.Ecom_Project.model.Product;

public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private String brand;
    private double price;
    private String category;
    private String imageName;
    private String imageType;

    public ProductDTO() {
        // Required for JSON deserialization
    }



    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.imageName = product.getImageName();
        this.imageType = product.getImageType();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    // Getters and Setters

    public Product toEntity() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setBrand(this.brand);
        product.setPrice(this.price);
        product.setCategory(this.category);
        product.setImageName(this.imageName);
        product.setImageType(this.imageType);
        // You may need to handle other fields like releaseDate, etc.
        return product;
    }
}
