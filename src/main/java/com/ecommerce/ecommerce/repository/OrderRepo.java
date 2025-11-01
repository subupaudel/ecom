package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order,Long>{
}
