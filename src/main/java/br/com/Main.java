package br.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.repository")
public class Main {

    public static void main(String[] args) {

//        args = new String[1];
//        args[0] = "--spring.profiles.active=dev";

        SpringApplication.run(Main.class, args);
    }

}