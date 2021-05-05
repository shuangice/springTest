package com.spring.springbucks.waiterservice.controller;

import com.spring.springbucks.waiterservice.controller.reuqest.NewOrderRequest;
import com.spring.springbucks.waiterservice.model.Coffee;
import com.spring.springbucks.waiterservice.model.CoffeeOrder;
import com.spring.springbucks.waiterservice.service.CoffeeOrderService;
import com.spring.springbucks.waiterservice.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService orderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    public CoffeeOrder getOne(@PathVariable("id") Long id) {
        return orderService.get(id);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder createOrder(@RequestBody NewOrderRequest newOrder) {
        Coffee[] coffees = coffeeService.getCoffeeByName(newOrder.getItems()).toArray(new Coffee[]{});
        return orderService.createOrder(newOrder.getCustomer(),coffees);
    }
}
