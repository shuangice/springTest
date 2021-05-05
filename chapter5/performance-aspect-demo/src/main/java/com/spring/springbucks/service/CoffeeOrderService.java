package com.spring.springbucks.service;

import com.spring.springbucks.model.Coffee;
import com.spring.springbucks.model.CoffeeOrder;
import com.spring.springbucks.model.OrderState;
import com.spring.springbucks.repository.CoffeeOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository orderRepository;

    public CoffeeOrder createOrder(String customer, Coffee...coffees) {
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffees))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = orderRepository.save(coffeeOrder);
        log.info("New Order: {}", saved);
        return saved;
    }

    public boolean updateState(CoffeeOrder coffeeOrder, OrderState orderState) {
        if (orderState.compareTo(coffeeOrder.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", orderState, coffeeOrder.getState());
            return false;
        }
        coffeeOrder.setState(orderState);
        orderRepository.save(coffeeOrder);
        log.info("Updated Order: {}", coffeeOrder);
        return true;
    }
}
