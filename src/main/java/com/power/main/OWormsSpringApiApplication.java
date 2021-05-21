package com.power.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.power")
@EnableJpaRepositories("com.power")
@ComponentScan("com.power")
@EntityScan("com.power")
public class OWormsSpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OWormsSpringApiApplication.class, args);
	}

}
