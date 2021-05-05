package com.spring.springbucks.waiterservice.repository;

import com.spring.springbucks.waiterservice.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
