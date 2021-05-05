package com.spring.springbucks.waiterservice.mapper;

import com.spring.springbucks.waiterservice.model.Coffee;
import com.spring.springbucks.waiterservice.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
