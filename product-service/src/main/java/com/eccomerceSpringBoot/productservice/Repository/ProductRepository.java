package com.eccomerceSpringBoot.productservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eccomerceSpringBoot.productservice.Model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
