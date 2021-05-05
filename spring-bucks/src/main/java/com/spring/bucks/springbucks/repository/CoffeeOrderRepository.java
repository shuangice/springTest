package com.spring.bucks.springbucks.repository;

import com.spring.bucks.springbucks.model.CoffeeOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CoffeeOrderRepository extends BaseRepository<CoffeeOrder, Long> {

    List<CoffeeOrder> findByCustomerOrderById(String customer);

    List<CoffeeOrder>findByItems_Name(String name);

}
