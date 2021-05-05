package com.spring.cache.springbucks.repository;

import com.spring.cache.springbucks.model.CoffeeCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache, Long> {
    public Optional<CoffeeCache> findOneByName(String name);
}
