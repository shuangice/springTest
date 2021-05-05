package com.spring.bucks.springbucks.repository;

import com.spring.bucks.springbucks.model.Coffee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface CoffeeRepository extends BaseRepository<Coffee, Long> {
}
