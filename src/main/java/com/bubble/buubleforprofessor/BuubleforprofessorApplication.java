package com.bubble.buubleforprofessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.bubble.buubleforprofessor.domain.university.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.bubble.buubleforprofessor.domain.university.repository.elasticsearch")
public class BuubleforprofessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuubleforprofessorApplication.class, args);
	}

}
