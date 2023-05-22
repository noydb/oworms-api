package com.oworms.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.oworms")
@EnableJpaRepositories("com.oworms")
@ComponentScan("com.oworms")
@EntityScan("com.oworms")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}