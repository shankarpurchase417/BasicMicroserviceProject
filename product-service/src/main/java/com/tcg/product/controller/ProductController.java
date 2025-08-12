package com.tcg.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcg.product.dto.ProductDto;
import com.tcg.product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.addProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}

