package com.alexwestcott.socialmedia.controller;

import java.io.IOException;

import com.alexwestcott.socialmedia.service.ImageService;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    private static final String BASE_URL = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    public HomeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = BASE_URL + "/" + FILENAME + "/raw",
                produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return imageService.findOneImage(filename)
                .map(
                        image -> {
                            try{
                                return ResponseEntity.ok()
                                        .contentLength(image.contentLength())
                                        .body(
                                            new InputStreamResource(
                                                image.getInputStream()
                                            )
                                        );
                            } catch(IOException e){
                                return ResponseEntity.badRequest()
                                        .body("Couldn't retrieve " + filename
                                        + ": " + e);

                            }
                        }
                );
    }

    @PostMapping(value = BASE_URL)
    public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }
//
//    @RequestMapping(path=BASE_URL, method=RequestMethod.POST)
//    public Mono<String> uploadFile(@RequestPart Flux<FilePart> files){
//        return imageService.createImage(files).then(Mono.just("redirect:/"));
//    }

    @DeleteMapping(BASE_URL + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename){
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> index(Model model){
        model.addAttribute("images", imageService.findAllImages());
        return Mono.just("index");
    }
}
