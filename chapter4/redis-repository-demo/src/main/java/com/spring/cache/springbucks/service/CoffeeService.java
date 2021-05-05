package com.spring.cache.springbucks.service;


import com.spring.cache.springbucks.model.Coffee;
import com.spring.cache.springbucks.model.CoffeeCache;
import com.spring.cache.springbucks.repository.CoffeeCacheRepository;
import com.spring.cache.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    private static final String CACHE = "springbucks-coffee";
    @Autowired
    private CoffeeCacheRepository cacheRepository;
    @Autowired
    private CoffeeRepository coffeeRepository;

    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public void reloadCoffee() {
    }

    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cached = cacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            CoffeeCache coffeeCache = cached.get();
            Coffee coffee = Coffee.builder()
                    .name(coffeeCache.getName())
                    .price(coffeeCache.getPrice())
                    .build();
            log.info("Coffee {} found by cache", coffeeCache);
            return Optional.of(coffee);
        }else {
            Optional<Coffee> oneCoffee = findOneCoffee(name);
            oneCoffee.ifPresent(coffee -> {
                CoffeeCache coffeeCache = CoffeeCache.builder()
                        .id(coffee.getId())
                        .name(coffee.getName())
                        .price(coffee.getPrice())
                        .build();
                log.info("Save Coffee {} to cache",coffeeCache);
                cacheRepository.save(coffeeCache);
            });
            return oneCoffee;
        }
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build()
                , matcher));
        log.info("Coffee: {}", coffee);
        return coffee;
    }
}
