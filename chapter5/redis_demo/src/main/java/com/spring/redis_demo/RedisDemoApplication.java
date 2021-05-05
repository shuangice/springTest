package com.spring.redis_demo;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class RedisDemoApplication implements ApplicationRunner {

    private static final String KEY = "COFFEE_MENU";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;


    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReactiveHashOperations<String, String, String> hash = redisTemplate.opsForHash();
        CountDownLatch downLatch = new CountDownLatch(2);

        List<Coffee> list = jdbcTemplate.query(
                "select * from t_coffee", (rs, i) ->
                        Coffee.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .price(rs.getLong("price"))
                                .build()
        );

        Flux.fromIterable(list)
                .publishOn(Schedulers.single())
                .doOnComplete(() -> log.info("list OK"))
                .flatMap(c -> {
                    log.info("try to put {} {}", c.getName(), c.getPrice());
                    return hash.put(KEY, c.getName(), c.getPrice().toString());
                })
                .doOnComplete(() -> log.info("set OK"))
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
                .doOnComplete(() -> log.info("expire OK"))
                .onErrorResume(e -> {
                    log.info("error {}", e.getMessage());
                    return Mono.just(false);
                })
                .subscribe(b -> log.info("Boolean {}", b),
                        e -> log.error("error {}", e.getMessage()),
                        () -> downLatch.countDown()
                );

        hash.get(KEY,"espresso").subscribeOn(Schedulers.single())
                .subscribe(v -> log.info("get value {},{}", v,downLatch.getCount()),
                        e-> log.error("error {}" , e.getMessage()),
                        () -> downLatch.countDown());

        log.info("Waiting");
        downLatch.await();



    }
}
