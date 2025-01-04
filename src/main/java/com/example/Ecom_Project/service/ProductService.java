package com.example.Ecom_Project.service;

import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        return repo.save(product);
    }

    public Product UpdateProduct(int id, Product product) {
        // Check if product exists by ID
        if (repo.existsById(id)) {
            // Set the ID of the incoming product to the one provided in the request
            product.setId(id);
            // Save the updated product
            return repo.save(product);
        } else {
            // Return null if product doesn't exist
            return null;
        }
    }

    public void DeleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        return  repo.searchProducts(keyword);
    }


//    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
//        System.out.println("This is Add Method");
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageDate(imageFile.getBytes());
//        return repo.save(product);
//    }
}
