package com.provectus.formula.alexis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication(scanBasePackages = "com")
@EnableJpaRepositories(basePackages = "com")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}