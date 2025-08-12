package com.tcg.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcg.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}

