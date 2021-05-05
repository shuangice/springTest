package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CoffeeRepository extends MongoRepository<Coffee, String> {
}
