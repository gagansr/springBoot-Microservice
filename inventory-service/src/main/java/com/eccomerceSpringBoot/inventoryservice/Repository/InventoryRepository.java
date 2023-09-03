package com.eccomerceSpringBoot.inventoryservice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eccomerceSpringBoot.inventoryservice.Model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
