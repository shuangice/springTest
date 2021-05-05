package com.spring.springbucks.waiterservice.controller.request;

import com.spring.springbucks.waiterservice.model.Coffee;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NewOrderRequest {
    private String customer;
    private List<String> items;

}
