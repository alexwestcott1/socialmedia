package com.alexwestcott.socialmedia.service;

import com.alexwestcott.socialmedia.domain.User;
import com.alexwestcott.socialmedia.repo.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findOneUser(String username) {
        return userRepository.findByUsername(username);
    }

    public Flux<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Mono<Void> createUser(User user){
        //todo: is this reactive??
//        return user.flatMap(
//                newUser -> {
//                    Mono<User> saveUser = userRepository.save(newUser);
//                    return Mono.when(saveUser);
//                }
//        ).then().log("User created");
        if(user.getUsername() == null || user.getJoinDate() == null){
            System.out.println("error! user is null");
            return null;
        }
        return userRepository.save(user).then();
    }

    public Mono<Void> deleteUser(String username) {
        Mono<Void> deleteDatabaseUser = userRepository.findByUsername(username).flatMap(userRepository::delete);

        return Mono.when(deleteDatabaseUser);
    }
}
