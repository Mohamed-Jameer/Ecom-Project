package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    @RequestMapping("/")
    public String home(){
        return "Welcome Our Page";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
     return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
       Product product = service.getProductById(id);
       if(product!=null) {
           return new ResponseEntity<>(product, HttpStatus.OK);
       }
       else{
           return new ResponseEntity<>( HttpStatus.NOT_FOUND);
       }
    }

//    @PostMapping("/addProduct")
//    public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
//                                        @RequestPart("imageFile") MultipartFile imageFile) {
//        System.out.println("This is Add Method");
//        try {
//            System.out.println("Received product: " + product);
//            System.out.println("Received file: " + imageFile.getOriginalFilename());
//
//            Product savedProduct = service.addProduct(product, imageFile);
//            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//        } catch (IOException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/addProduct")
    public Product addProduct(@RequestBody Product product) {
        return  service.addProduct(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> UpdateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updatedProduct = service.UpdateProduct(id, product);

        if (updatedProduct != null) {
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping  ("/products/{id}")
    public  ResponseEntity<String> DeleteProduct(@PathVariable int id) {
        Product product1 = service.getProductById(id);
        if(product1 != null) {
            service.DeleteProduct(id);
            return  new ResponseEntity<>("Deleted" , HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>("Not Deleted" , HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> products = service.searchProduct(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }




}
