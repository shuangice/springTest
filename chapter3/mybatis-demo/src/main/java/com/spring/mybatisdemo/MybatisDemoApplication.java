package com.spring.mybatisdemo;

import com.spring.mybatisdemo.mapper.CoffeeMapper;
import com.spring.mybatisdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
@MapperScan("com.spring.mybatisdemo.mapper")
public class MybatisDemoApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CoffeeMapper coffeeMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MybatisDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //log.info("databaseInfo: {}",dataSource.toString());


        Coffee coffee = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
        int count = coffeeMapper.save(coffee);
        log.info("Save: {} {}", count, coffee);

        coffee = Coffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0)).build();
        count = coffeeMapper.save(coffee);
        log.info("Save {} {}", count, coffee);

        coffee = coffeeMapper.findById(coffee.getId());
        log.info("findById: {}", coffee);
    }



}
