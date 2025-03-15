package com.discgolf.in_the_bag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.discgolf.in_the_bag")
@EnableJpaRepositories("com.discgolf.in_the_bag.repositories")
@EntityScan("com.discgolf.in_the_bag.models")
public class InTheBagApplication {
	public static void main(String[] args) {
		SpringApplication.run(InTheBagApplication.class, args);
	}
}
