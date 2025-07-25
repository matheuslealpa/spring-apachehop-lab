package com.exemplo.springapachehoplab.app.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.hop.core.HopClientEnvironment;
import org.apache.hop.core.exception.HopException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HopInitializer {

    @PostConstruct
    public void initHop() throws HopException {
        if (!HopClientEnvironment.isInitialized()) {
            HopClientEnvironment.init();
        }
    }
}
