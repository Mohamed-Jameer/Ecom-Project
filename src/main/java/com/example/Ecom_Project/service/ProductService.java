package com.example.Ecom_Project.service;

import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

//    public Product addProduct(Product product, MultipartFile imageFile) {
//        return repo.save(product);
//    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException, IOException {
        System.out.println("Product Service");
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageDate(imageFile.getBytes());
        return repo.save(product);
    }

    public Product UpdateProduct(int id, Product product) {
        if (repo.existsById(id)) {
            product.setId(id);
            return repo.save(product);
        } else {
            return null;
        }
    }

    public void DeleteProduct(int id) {
        repo.deleteById(id);
    }
    public List<Product> searchProduct(String keyword) {
        return  repo.searchProducts(keyword);
    }

    public List<Product> getProductName(String name) {
        List<Product> products = getAllProducts();
        List<Product> addProducts =  new ArrayList<>();
        for(Product product : products){
            if(product.getName().equalsIgnoreCase(name) || product.getCategory().equalsIgnoreCase(name) || product.getBrand().equalsIgnoreCase(name)){
                 addProducts.add(product);
            }
        }
        return addProducts;
    }


//    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
//        System.out.println("This is Add Method");
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageDate(imageFile.getBytes());
//        return repo.save(product);
//    }
}
