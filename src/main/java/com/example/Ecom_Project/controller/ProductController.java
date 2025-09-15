package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.ProductDTO;
import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.service.ProductService;
import com.example.Ecom_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private UserService userService;

    @GetMapping("/test-exception")
    public void testException() {
        service.throwTestException();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable int id) {
        ProductDTO product = service.getProductById(id);
        return product != null ?
                ResponseEntity.ok(product) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestPart("product") String productJson,
                                        @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productJson, Product.class);
            Product saved = service.addProduct(product, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(saved));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updated = service.updateProduct(id, product);
        return updated != null ?
                ResponseEntity.ok("Product updated successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product found = service.getProductEntityById(id);
        if (found != null) {
            service.deleteProduct(id);
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.badRequest().body("Product not found");
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchProduct(keyword));
    }

//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @GetMapping("/searchName")
//    public ResponseEntity<List<ProductDTO>> getProductName(@RequestParam String name) {
//        return ResponseEntity.ok(service.getProductName(name));
//    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        Product product = service.getProductEntityById(id);
        if (product == null || product.getImageDate() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, product.getImageType())
                .body(product.getImageDate());
    }

    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return service.getFeaturedProducts();
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return service.getAllCategories();
    }
}
