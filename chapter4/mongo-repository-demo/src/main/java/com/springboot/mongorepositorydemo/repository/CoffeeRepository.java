package com.springboot.mongorepositorydemo.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.springboot.mongorepositorydemo.model.Coffee;
import java.util.List;

public interface CoffeeRepository extends MongoRepository<Coffee, String> {
    List<Coffee> findByName(String name);
}
