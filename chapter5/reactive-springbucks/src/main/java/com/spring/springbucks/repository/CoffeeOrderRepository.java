package com.spring.springbucks.repository;

import com.spring.springbucks.model.CoffeeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Repository
public class CoffeeOrderRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Long> save(CoffeeOrder order) {
        return databaseClient.insert().into("t_order")
                .value("customer", order.getCustomer())
                .value("state",  order.getState().ordinal())
                .value("create_time", new Timestamp(order.getCreateTime().getTime()))
                .value("update_time", new Timestamp(order.getUpdateTime().getTime()))
                .fetch()
                .first()
                .flatMap(m -> Mono.just((Long) m.get("ID")) )
                .flatMap(id -> Flux.fromIterable(order.getItems())
                .flatMap(coffee -> databaseClient.insert().into("t_order_coffee")
                        .value("coffee_order_id", id)
                        .value("items_id", coffee.getId())
                        .then()).then(Mono.just(id)));
    }
}
