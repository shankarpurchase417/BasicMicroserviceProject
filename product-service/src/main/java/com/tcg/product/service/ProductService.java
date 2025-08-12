package com.tcg.product.service;

import com.tcg.product.dto.ProductDto;

public interface ProductService {
	
    ProductDto addProduct(ProductDto dto);
    
    ProductDto getProductById(int id);
}

