package com.spring.springbucks.service;

import com.spring.springbucks.model.Coffee;
import com.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class CoffeeService {

    private static final String PREFIX = "springbucks-";

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private ReactiveRedisTemplate<String, Coffee> redisTemplate;


    public Flux<Boolean> initCache() {
        return coffeeRepository.findAll()
                .flatMap(coffee -> redisTemplate.opsForValue()
                        .set(PREFIX + coffee.getName(), coffee)
                .flatMap(b-> redisTemplate.expire(PREFIX+coffee.getName(), Duration.ofMinutes(1)))
                .doOnSuccess(v->log.info("Loading and caching {}",coffee)));
    }

    public Mono<Coffee> findOneCoffee(String name) {
        return redisTemplate.opsForValue().get(PREFIX+name)
                .switchIfEmpty(coffeeRepository.findByName(name)
                        .doOnSuccess(s -> log.info("Loading {} from DB.", name)));
    }
}
