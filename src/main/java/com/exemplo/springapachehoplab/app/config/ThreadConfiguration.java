package com.exemplo.springapachehoplab.app.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfiguration {

    @Bean(name = "virtualExecutor")
    public Executor t() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
