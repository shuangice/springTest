package geektime.spring.springbucks.service;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeCache;
import geektime.spring.springbucks.repository.CoffeeCacheRepository;
import geektime.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeCacheRepository coffeeCacheRepository;

    public void saveAll(List<Coffee> list) {
        coffeeRepository.saveAll(list);
    }

    public List<Coffee> findAll() {
        return coffeeRepository.findAll();
    }

    public void deleteAll() {
        coffeeRepository.deleteAll();
        coffeeCacheRepository.deleteAll();
    }

    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cached = coffeeCacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            //存在从缓存中取
            CoffeeCache coffeeCache = cached.get();
            Coffee coffee = Coffee.builder()
                    .name(coffeeCache.getName())
                    .price(coffeeCache.getPrice())
                    .build();
            log.info("found coffee{} by cache",coffee);
            return Optional.of(coffee);
        }else {
            //不存在 查询数据库 放入内存
            Optional<Coffee> oneCoffee = findOneCoffee(name);
            oneCoffee.ifPresent(coffee -> {
                CoffeeCache coffeeCache = CoffeeCache.builder()
                        .id(coffee.getId())
                        .name(coffee.getName())
                        .price(coffee.getPrice())
                        .build();
                log.info("Save Coffee {} to cache",coffeeCache);
                coffeeCacheRepository.save(coffeeCache);
            });
            return oneCoffee;
        }
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }
}
