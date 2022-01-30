package com.power.oworms.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.power.oworms")
@EnableJpaRepositories("com.power.oworms")
@ComponentScan("com.power.oworms")
@EntityScan("com.power.oworms")
public class OWormsSpringApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OWormsSpringApiApplication.class, args);
    }

}
