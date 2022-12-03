package com.example.restapijwttutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class RestApiJwtTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiJwtTutorialApplication.class, args);
	}

}
