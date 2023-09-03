package com.eccomerceSpringBoot.productservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eccomerceSpringBoot.productservice.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
