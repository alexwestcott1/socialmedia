package com.alexwestcott.socialmedia.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void userShouldHaveProperties(){
        //given
        User expectedUser = new User("1", "alex", LocalDate.of(2015, 05, 20));

        //when, then
        assertThat(expectedUser.getId()).isEqualTo("1");
        assertThat(expectedUser.getJoinDate()).isEqualTo(LocalDate.of(2015, 05, 20));
        assertThat(expectedUser.getUsername()).isEqualTo("alex");
    }
}
