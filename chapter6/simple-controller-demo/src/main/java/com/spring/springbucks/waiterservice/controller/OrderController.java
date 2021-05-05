package com.spring.springbucks.waiterservice.controller;

import com.spring.springbucks.waiterservice.controller.request.NewOrderRequest;
import com.spring.springbucks.waiterservice.model.Coffee;
import com.spring.springbucks.waiterservice.model.CoffeeOrder;
import com.spring.springbucks.waiterservice.service.CoffeeService;
import com.spring.springbucks.waiterservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoffeeService coffeeService;

    @RequestMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder) {
        log.info("Receive new Order {}", newOrder);
        Coffee[] coffees = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[]{});
        return orderService.createOrder(newOrder.getCustomer(), coffees);
    }
}
