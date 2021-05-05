package com.spring.cache.springbucks;

import com.spring.cache.springbucks.converter.BytesToMoneyConverter;
import com.spring.cache.springbucks.converter.MoneyToBytesConverter;
import com.spring.cache.springbucks.model.Coffee;
import com.spring.cache.springbucks.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
@SpringBootApplication
public class SpringbucksApplication implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;

    @Bean
    public RedisTemplate<String, Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Coffee> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(),new BytesToMoneyConverter()));
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbucksApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Coffee> mocha = coffeeService.findSimpleCoffeeFromCache("mocha");
        log.info("Coffee {}", mocha);

        for (int i = 0; i < 5; i++) {
          mocha = coffeeService.findSimpleCoffeeFromCache("mocha");
        }

        log.info("Coffee {}", mocha);
    }
}
