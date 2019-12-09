package com.alexwestcott.socialmedia.repo;

import com.alexwestcott.socialmedia.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoOperations operations;

    @BeforeEach
    public void setUp(){
        operations.dropCollection(User.class);
        operations.insert(new User("1", "alex", LocalDate.of(2015, 5, 20)));
        operations.insert(new User("2", "bob", LocalDate.of(2016, 11, 1)));
        operations.insert(new User("3", "clive", LocalDate.of(2019, 2, 3)));
        operations.findAll(User.class).forEach(result -> System.out.println(result.toString()));
    }

    @Test
    public void shouldFindOneUser(){
        Mono<User> user = userRepository.findByUsername("alex");
        StepVerifier.create(user).expectNextMatches(result -> {
                assertThat(result.getId()).isEqualTo("1");
                assertThat(result.getUsername()).isEqualTo("alex");
                assertThat(result.getJoinDate()).isEqualTo(LocalDate.of(2015, 5, 20));
                return true;
            }
        );
    }

    @Test
    public void shouldFindAllUsers(){
        Flux<User> users = userRepository.findAll();
        StepVerifier.create(users).recordWith(ArrayList::new).expectNextCount(3)
                .consumeRecordedWith(results -> {
                    assertThat(results).hasSize(3);
                    assertThat(results).extracting(User::getUsername).contains("alex", "bob", "clive");
                })
                .expectComplete()
                .verify();
    }

}
