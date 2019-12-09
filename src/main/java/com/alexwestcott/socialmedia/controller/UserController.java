package com.alexwestcott.socialmedia.controller;

import com.alexwestcott.socialmedia.domain.User;
import com.alexwestcott.socialmedia.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Mono<String> index(Model model){
        model.addAttribute("users", userService.findAllUsers());
        return Mono.just("users");
    }

    @GetMapping("/users/{username}")
    public Mono<ResponseEntity<User>> getUserFromName(@PathVariable("username") String username){
        return userService.findOneUser(username).map(
                user -> ResponseEntity.ok().body(user)
        );
    }

    @DeleteMapping("/users/{username}")
    public Mono<String> deleteUser(@PathVariable("username") String username){
        return userService.deleteUser(username).then(Mono.just("redirect:/"));
    }

}
