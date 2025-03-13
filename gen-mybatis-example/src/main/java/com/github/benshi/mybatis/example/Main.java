package com.github.benshi.mybatis.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // @Bean
    // public ObjectMapper objectMapper() {
    // ObjectMapper mapper = new ObjectMapper();
    // mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
    // mapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    // mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    // return mapper;
    // }
}