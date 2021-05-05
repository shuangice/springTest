package com.spring.springbucks.waiterservice.dao;

import com.spring.springbucks.waiterservice.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
    public Coffee findByName(String name);

    public List<Coffee> findByNameInOrderById(List<String> names);
}
