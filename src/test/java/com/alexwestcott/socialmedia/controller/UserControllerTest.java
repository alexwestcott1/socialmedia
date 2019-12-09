package com.alexwestcott.socialmedia.controller;

import com.alexwestcott.socialmedia.domain.User;
import com.alexwestcott.socialmedia.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = UserController.class)
@Import({ThymeleafAutoConfiguration.class})
public class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnUserFromUsername(){
        //given
        String username = "alex";
        String uri = "/users/" + username;
        when(userService.findOneUser(username)).thenReturn(Mono.just(new User("1", "alex", LocalDate.of(2015, 5, 25))));

        //when
        EntityExchangeResult<String> response = webTestClient.get().uri(uri).exchange().expectStatus().isOk().expectBody(String.class).returnResult();

        //then
        verify(userService).findOneUser(username);
        verifyNoMoreInteractions(userService);
        System.out.println(response.getResponseBody());
    }

    @Test
    public void shouldReturnAllUsers(){
        //given
        String uri = "/users";
        User mockUser1 = new User("1", "alex", LocalDate.of(2019, 2, 1));
        User mockUser2 = new User("2", "bob", LocalDate.of(2018, 7, 5));
        when(userService.findAllUsers()).thenReturn(Flux.just(mockUser1, mockUser2));

        //when
        EntityExchangeResult<String> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        //then
        verify(userService).findAllUsers();
        verifyNoMoreInteractions(userService);
        assertThat(response.getResponseBody()).contains("alex", "bob");
    }

    @Test
    public void shouldDeleteUser(){
        //given
        String username = "clive";
        String uri = "/users/" + username;

        when(userService.deleteUser(username)).thenReturn(Mono.empty());

        //when
        webTestClient.delete().uri(uri).exchange().expectStatus().isSeeOther();

        //then
        verify(userService).deleteUser(username);
        verifyNoMoreInteractions(userService);

    }
}
