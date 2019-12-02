package com.alexwestcott.socialmedia.repo;

import com.alexwestcott.socialmedia.domain.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class ImageRepositoryTest {

    @Autowired
    ImageRepository repository;

    @Autowired
    MongoOperations operations;

    @BeforeEach
    public void setUp(){
        operations.dropCollection(Image.class);
        operations.insert(new Image("1", "dog.jpg"));
        operations.insert(new Image("2", "cat.jpg"));
        operations.insert(new Image("3", "flamingo.jpg"));
        operations.findAll(Image.class).forEach(image -> {
            System.out.println(image.toString());
        });
    }

    @Test
    public void findAllShouldWork(){
        Flux<Image> images = repository.findAll();
        StepVerifier.create(images)
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(results -> {
                    assertThat(results).hasSize(3);
                    assertThat(results).extracting(Image::getName).contains("dog.jpg", "cat.jpg", "flamingo.jpg");
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findByNameShouldWork(){
        Mono<Image> image = repository.findByName("dog.jpg");
        StepVerifier.create(image)
                .expectNextMatches(results -> {
                    assertThat(results.getName()).isEqualTo("dog.jpg");
                    assertThat(results.getId()).isEqualTo("1");
                    return true;
                });
    }

    @Test
    public void deleteImageShouldWork(){
        Mono<Void> delete = repository.deleteById("1");
        StepVerifier.create(delete)
                .verifyComplete();
    }
}
