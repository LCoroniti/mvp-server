package com.tus.traunreut;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebserverApplication.class, args);
    }
}
