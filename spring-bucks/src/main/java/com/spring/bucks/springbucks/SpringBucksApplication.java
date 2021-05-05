package com.spring.bucks.springbucks;

import com.spring.bucks.springbucks.model.Coffee;
import com.spring.bucks.springbucks.model.CoffeeOrder;
import com.spring.bucks.springbucks.model.OrderState;
import com.spring.bucks.springbucks.repository.CoffeeOrderRepository;
import com.spring.bucks.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {


    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBucksApplication.class, args);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        initOrder();
        findOrders();

    }

    private void initOrder() {
        Coffee espresso = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .build();
        coffeeRepository.save(espresso);
        log.info("Coffee: {}", espresso);

        Coffee latte = Coffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .build();
        coffeeRepository.save(latte);
        log.info("Coffee: {}", latte);

        log.info("tableInfo: {}",
                coffeeRepository.findAll());

        CoffeeOrder la_la = CoffeeOrder.builder().customer("La la")
                .items(Collections.singletonList(espresso))
                .state(OrderState.INIT).build();
        coffeeOrderRepository.save(la_la);
        log.info("CoffeeOrder: {}", la_la);
        la_la = CoffeeOrder.builder().customer("La la")
                .items(Arrays.asList(latte, espresso))
                .state(OrderState.INIT).build();
        coffeeOrderRepository.save(la_la);
        log.info("CoffeeOrder: {}", la_la);
    }

    private void findOrders() {
        //打印所有coffee类型
        coffeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .forEach(c -> log.info("Loading: {}", c));

        //打印当前的orderId
        List<CoffeeOrder> orderList = coffeeOrderRepository.findTop3ByOrderByUpdateTimeDescIdAsc();
        log.info("findTop3ByOrderByUpdateTimeDescIdAsc: {}", getJoinedOrderId(orderList));

        //打印通过消费者名称获取的orderId
        orderList = coffeeOrderRepository.findByCustomerOrderById("La la");
        log.info("findByCustomerOrderById: {}", getJoinedOrderId(orderList));

        //不开启事务 因为没Session而报LazyInitializationException
        orderList.forEach(o-> {
            log.info("oder: {}", o.getId());
            o.getItems().forEach(i->log.info("Item: {}", i));
        });

        //打印通过coffee名称获取的订单
        orderList = coffeeOrderRepository.findByItems_Name("latte");
        log.info("findByItems_Name: {}", getJoinedOrderId(orderList));
    }

    private String getJoinedOrderId(List<CoffeeOrder> list) {
        return list.stream().map(o -> o.getId().toString())
                .collect(Collectors.joining(","));


    }
}
