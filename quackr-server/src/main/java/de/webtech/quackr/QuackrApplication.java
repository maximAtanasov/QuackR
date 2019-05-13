package de.webtech.quackr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("de.webtech.quackr.persistence")
@ComponentScan("de.webtech.quackr")
public class QuackrApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuackrApplication.class, args);
	}

}
