package com.alexwestcott.socialmedia.config;

import com.alexwestcott.socialmedia.domain.Chapter;
import com.alexwestcott.socialmedia.repo.ChapterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner init(ChapterRepository repository){
        return args ->{
            Flux.just(
                    new Chapter("Quick Start With Java"),
                    new Chapter("Reactive Web with Spring Boot"),
                    new Chapter("More book chapters")
            )
                    .flatMap(repository::save)
                    .subscribe(System.out::println);
        };
    }
}
