package com.tcg.product.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcg.product.dto.ProductDto;
import com.tcg.product.entity.Product;
import com.tcg.product.repository.ProductRepository;
import com.tcg.product.service.ProductService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        Product saved = repo.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    @Retry(name = "productService")
    public ProductDto getProductById(int id) {
        // 🔹 Test ke liye forced failure
        if (id == 99) {
            throw new RuntimeException("Forced failure for testing CircuitBreaker");
        }

        // 🔹 Normal DB fetch
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDto.class);
    }

    // 🔹 Fallback method (same params + Throwable at last)
    public ProductDto getProductFallback(int id, Throwable ex) {
        ProductDto fallbackProduct = new ProductDto();
        fallbackProduct.setProductId(id);
        fallbackProduct.setName("Default Product");
        fallbackProduct.setPrice(0.0);
        return fallbackProduct;
    }
}


