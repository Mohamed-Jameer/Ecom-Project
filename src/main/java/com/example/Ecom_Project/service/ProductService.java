package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.ProductDTO;
import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    @Cacheable(value = "products")
    public List<ProductDTO> getAllProducts() {
        return repo.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(int id) {
        return repo.findById(id)
                .map(ProductDTO::new)
                .orElse(null);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageDate(imageFile.getBytes());
        return repo.save(product);
    }

    @CacheEvict(value = {"products", "product"}, key = "#id")
    public Product updateProduct(int id, Product product) {
        if (repo.existsById(id)) {
            product.setId(id);
            return repo.save(product);
        }
        return null;
    }

    @CacheEvict(value = {"products", "product"}, key = "#id")
    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<ProductDTO> searchProduct(String keyword) {
        return repo.searchProducts(keyword)
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductName(String name) {
        return repo.findAll()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name) ||
                        p.getBrand().equalsIgnoreCase(name) ||
                        p.getCategory().equalsIgnoreCase(name))
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    public Product getProductEntityById(int id) {
        return repo.findById(id).orElse(null);
    }

    public void throwTestException() {
        throw new RuntimeException("This is a test exception for AOP");
    }
}
