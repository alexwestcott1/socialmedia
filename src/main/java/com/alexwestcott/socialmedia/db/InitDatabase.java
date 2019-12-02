package com.alexwestcott.socialmedia.db;

import com.alexwestcott.socialmedia.domain.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

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
        };
    }
}
