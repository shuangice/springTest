package geektime.spring.springbucks;

import geektime.spring.springbucks.converter.BytesToMoneyConverter;
import geektime.spring.springbucks.converter.MoneyReadConverter;
import geektime.spring.springbucks.converter.MoneyToBytesConverter;
import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.service.CoffeeOrderService;
import geektime.spring.springbucks.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories
@EnableRedisRepositories
public class SpringBucksApplication implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService orderService;

   /* @Bean
    public RedisTemplate<String, CoffeeCache> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, CoffeeCache> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }*/

    @Bean
    public RedisCustomConversions customConversions() {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter())
        );
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBucksApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        coffeeService.deleteAll();

        addCoffee();
        Optional<Coffee> espresso = coffeeService.findSimpleCoffeeFromCache("espresso");
        Optional<Coffee> mocha = coffeeService.findSimpleCoffeeFromCache("mocha");

        for (int i = 0; i < 5; i++) {
            espresso = coffeeService.findSimpleCoffeeFromCache("espresso");
        }
    }

    private void addCoffee() {
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(new Date())
                .updateTime(new Date()).build();
        Coffee latte = Coffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .createTime(new Date())
                .updateTime(new Date()).build();
        Coffee capuccino = Coffee.builder()
                .name("capuccino")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .createTime(new Date())
                .updateTime(new Date()).build();
        Coffee mocha = Coffee.builder()
                .name("mocha")
                .price(Money.of(CurrencyUnit.of("CNY"), 35.0))
                .createTime(new Date())
                .updateTime(new Date()).build();
        Coffee macchiato = Coffee.builder()
                .name("macchiato")
                .price(Money.of(CurrencyUnit.of("CNY"), 25.0))
                .createTime(new Date())
                .updateTime(new Date()).build();

        coffeeService.saveAll(Arrays.asList(espresso, latte, capuccino, mocha, macchiato));
        coffeeService.findAll().forEach(coffee -> log.info("Coffee: {}", coffee));

    }

}

