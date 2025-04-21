package com.bubble.bubbleforprofessor;

import com.bubble.bubbleforprofessor.university.repository.es.UniversityElasticSearchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "com.bubble.bubbleforprofessor",
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = UniversityElasticSearchRepository.class
		)
)
@EnableElasticsearchRepositories(basePackages = "com.bubble.bubbleforprofessor.university.repository.es")
public class BubbleforprofessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BubbleforprofessorApplication.class, args);
	}

}
