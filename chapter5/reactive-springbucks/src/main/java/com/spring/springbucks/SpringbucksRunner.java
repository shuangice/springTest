package com.spring.springbucks;

import com.spring.springbucks.model.Coffee;
import com.spring.springbucks.model.CoffeeOrder;
import com.spring.springbucks.model.OrderState;
import com.spring.springbucks.service.CoffeeService;
import com.spring.springbucks.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Component
public class SpringbucksRunner implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private OrderService orderService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        coffeeService.initCache()
                .then( coffeeService.findOneCoffee("mocha")
                .flatMap(coffee -> {
                    CoffeeOrder order = createOrder("Li lee", coffee);
                    return orderService.create(order);
                })
                .doOnError(throwable -> log.info("error {}", throwable)))
                .subscribe(o-> log.info("Create Order {}", o));
        log.info("After Subscribe");
        Thread.sleep(5000);
    }

    private CoffeeOrder createOrder(String customer, Coffee... coffee) {
        return CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }
}
