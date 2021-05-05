package com.spring.springbucks.waiterservice.controller;

import com.spring.springbucks.waiterservice.controller.request.NewCoffeeRequest;
import com.spring.springbucks.waiterservice.model.Coffee;
import com.spring.springbucks.waiterservice.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffee(@Valid NewCoffeeRequest coffeeRequest,
                            BindingResult result) {
        if (result.hasErrors()) {
            //
            log.warn("Binding Errors: {}", result);
            return null;
        }
        return coffeeService.saveCoffee(coffeeRequest.getName(), coffeeRequest.getPrice());
    }

    //    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.CREATED)
//    public Coffee addCoffeeWithoutBindingResult(@Valid NewCoffeeRequest newCoffee) {
//        return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
//    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<Coffee> batchAddCoffee(@RequestParam("file") MultipartFile file) {
        List<Coffee> coffees = new ArrayList<>();
        if (!file.isEmpty()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

                String str;
                while ((str = reader.readLine()) != null) {
                    String[] split = StringUtils.split(str, " ");
                    if (split != null && split.length == 2) {
                        coffees.add(coffeeService.saveCoffee(split[0],
                                Money.of(CurrencyUnit.of("CNY"),
                                        NumberUtils.createBigDecimal(split[1]))));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return coffees;
    }

    @GetMapping(value = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable("id") Long id) {
        return coffeeService.getCoffee(id);
    }

    @GetMapping(value = "/", params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam("name") String name) {
        return coffeeService.getCoffee(name);
    }
}
