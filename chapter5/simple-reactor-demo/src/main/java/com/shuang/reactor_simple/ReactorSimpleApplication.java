package com.shuang.reactor_simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
@SpringBootApplication
public class ReactorSimpleApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReactorSimpleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n))
                .publishOn(Schedulers.elastic())
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .map(integer -> {
                    log.info("Publish {}, {}", Thread.currentThread(), integer);
                    return integer;
//                    return 10/(integer-3);
                })
                .doOnComplete(()-> log.info("Publisher COMPLETE 2"))
                .subscribeOn(Schedulers.single())
//                .onErrorResume(e-> {
//                    log.error("error {}", e.toString());
//                     return Mono.just(-1);
//                })
                .onErrorReturn(-1)
                .subscribe(i-> log.info("Subscribe {}, {}",Thread.currentThread(),i),
                        a-> log.error("error {}", a.toString()),
                        ()->log.info("Subscribe COMPLETE"),
                        s-> s.request(4));
        Thread.sleep(2000);
    }
}
