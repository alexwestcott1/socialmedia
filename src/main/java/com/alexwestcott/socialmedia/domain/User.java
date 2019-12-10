package com.alexwestcott.socialmedia.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public class User {
    @Id private String id;
    private final String username;
    private final LocalDate joinDate;

    public User(String id, String username, LocalDate joinDate) {
        this.id = id;
        this.username = username;
        this.joinDate = joinDate;
    }
}
