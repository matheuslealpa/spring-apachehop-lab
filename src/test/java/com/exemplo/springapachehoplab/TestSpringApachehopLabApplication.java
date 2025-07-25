package com.exemplo.springapachehoplab;

import org.springframework.boot.SpringApplication;

public class TestSpringApachehopLabApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringApachehopLabApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
