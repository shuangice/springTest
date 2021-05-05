package com.spring.mongo_demo;

import com.spring.mongo_demo.converter.MoneyReadConverter;
import com.spring.mongo_demo.converter.MoneyWriteConverter;
import com.spring.mongo_demo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class MongoDemoApplication implements ApplicationRunner {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    private CountDownLatch downLatch = new CountDownLatch(1);

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new MoneyReadConverter(), new MoneyWriteConverter()
        ));
    }

    public static void main(String[] args) {
        SpringApplication.run(MongoDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        startFromInsert(() -> log.info("Runnable"));

        startFromInsert(()->{log.info("Runnable");
        decreaseHighPrice();});
//        decreaseHighPrice();

        log.info("after start");

        downLatch.await();
    }

    //向数据库插入数据
    public void startFromInsert(Runnable runnable) {
        mongoTemplate.insertAll(initCoffee())
                .publishOn(Schedulers.elastic())
                .doOnNext(coffee -> log.info("next Coffee {}", coffee))
                .doOnComplete(runnable)
                .doFinally(c -> {
                    downLatch.countDown();
                    log.info("finally 1, {}", c);
                })
                .count()
                .subscribe(c -> log.info("Insert {} records", c));
    }

    public void decreaseHighPrice() {
        mongoTemplate.updateMulti(Query.query(Criteria.where("price").gte(3000L)),
                new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 10)).currentDate("updateTime")
                , Coffee.class)
        .doFinally(c->{
            downLatch.countDown();
            log.info("finally 2, {}", c);
        })
        .subscribe(r-> log.info("Result is {}", r));
    }

    //初始化测试参数
    private List<Coffee> initCoffee() {
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        Coffee latte = Coffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        return Arrays.asList(espresso, latte);
    }
}
