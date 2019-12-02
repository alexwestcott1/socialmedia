package com.alexwestcott.socialmedia.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTest {

    @Test
    public void imageShouldHaveProperties(){
        Image image = new Image("123", "name.jpg");
        assertThat(image.getId()).isEqualTo("123");
        assertThat(image.getName()).isEqualTo("name.jpg");
    }
}
