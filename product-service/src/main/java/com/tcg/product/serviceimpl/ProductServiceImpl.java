package com.tcg.product.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcg.product.dto.ProductDto;
import com.tcg.product.entity.Product;
import com.tcg.product.repository.ProductRepository;
import com.tcg.product.service.ProductService;

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
    public ProductDto getProductById(int id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDto.class);
    }
}

