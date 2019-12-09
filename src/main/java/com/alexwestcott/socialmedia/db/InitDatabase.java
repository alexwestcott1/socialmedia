package com.alexwestcott.socialmedia.db;

import com.alexwestcott.socialmedia.domain.Image;
import com.alexwestcott.socialmedia.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InitDatabase {
    @Bean
    CommandLineRunner init(MongoOperations operations){
        return args -> {
          operations.dropCollection(Image.class);
          operations.insert(new Image("1", "dog.jpg"));
          operations.insert(new Image("2", "cat.jpg"));
          operations.insert(new Image("3", "flamingo.jpg"));
          operations.findAll(Image.class).forEach(
                  image -> {System.out.println(image.toString());}
          );
          operations.dropCollection(User.class);
          operations.insert(new User("1", "alex", LocalDate.of(2015, 5, 20)));
          operations.insert(new User("2", "bob", LocalDate.of(2016, 11, 1)));
          operations.insert(new User("3", "clive", LocalDate.of(2019, 2, 3)));
          operations.findAll(User.class).forEach(
                  user -> {
                      System.out.println(user.toString());
                  }
          );
        };
    }
}
