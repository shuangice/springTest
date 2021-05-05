package com.spring.springbucks.service;

import com.spring.springbucks.model.CoffeeOrder;
import com.spring.springbucks.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private CoffeeOrderRepository orderRepository;

    public Mono<Long> create(CoffeeOrder order) {
       return  orderRepository.save(order);
    }
}
