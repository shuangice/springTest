package com.springbucks.mongodemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document //标明和coffee的collection对应
public class Coffee {

    @Id
    private String id; //表示为Coffee的Id，通过@Id注解，spring.data.mongo会把该id转换成mongodb中的Object ID
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
