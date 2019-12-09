package com.alexwestcott.socialmedia.controller;

import com.alexwestcott.socialmedia.domain.Image;
import com.alexwestcott.socialmedia.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers =  HomeController.class)
@Import({ThymeleafAutoConfiguration.class})
public class HomeControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private ImageService imageService;

    @Test
    public void shouldReturnImageFromId() {
        //given
        String filename = "flamingo";
        String uri = "/images/" + filename + "/raw";

        when(imageService.findOneImage(filename)).thenReturn(Mono.just(new ByteArrayResource("data".getBytes())));

        //when
        EntityExchangeResult<String> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        //then
        verify(imageService).findOneImage(filename);
        verifyNoMoreInteractions(imageService);
    }

    @Test
    public void shouldReturnAllImages() {
        //given
        String uri = "/";
        Image mockImage1 = new Image("1", "cat.jpg");
        Image mockImage2 = new Image("2", "dog.jpg");

        when(imageService.findAllImages()).thenReturn(Flux.just(mockImage1, mockImage2));

        //when
        EntityExchangeResult<String> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        //then
        verify(imageService).findAllImages();
        verifyNoMoreInteractions(imageService);
        assertThat(response.getResponseBody())
                .contains("<a href=\"/images/cat.jpg/raw\">")
                .contains("<a href=\"/images/dog.jpg/raw\">");

    }

    @Test
    public void deleteImageShouldWork() {
        //given
        String filename = "dog.jpg";
        String uri = "/images/" + filename;
        when(imageService.deleteImage(filename)).thenReturn(Mono.empty());

        //when
        webTestClient.delete().uri(uri).exchange()
                .expectStatus().isSeeOther();

        //then
        verify(imageService).deleteImage(filename);
        verifyNoMoreInteractions(imageService);

    }

}
