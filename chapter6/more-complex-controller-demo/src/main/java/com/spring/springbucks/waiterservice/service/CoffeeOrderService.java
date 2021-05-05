package com.spring.springbucks.waiterservice.service;

import com.spring.springbucks.waiterservice.dao.CoffeeOrderRepository;
import com.spring.springbucks.waiterservice.model.Coffee;
import com.spring.springbucks.waiterservice.model.CoffeeOrder;
import com.spring.springbucks.waiterservice.model.CoffeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CoffeeOrderService {

    @Autowired
    private CoffeeOrderRepository orderRepository;

    public CoffeeOrder get(Long id) {
        return orderRepository.getOne(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        CoffeeOrder order = CoffeeOrder.builder().customer(customer)
                .items(Arrays.asList(coffee))
                .state(CoffeeState.INIT)
                .build();

        CoffeeOrder save = orderRepository.save(order);
        log.info("NEW Order: {}", save);
        return save;
    }

    public boolean updateState(CoffeeOrder coffeeOrder, CoffeeState state) {
        if (state.compareTo(coffeeOrder.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", state, coffeeOrder.getState());
            return false;
        }

        coffeeOrder.setState(state);
        orderRepository.save(coffeeOrder);
        log.info("Updated Order: {}", coffeeOrder);
        return true;
    }
}
