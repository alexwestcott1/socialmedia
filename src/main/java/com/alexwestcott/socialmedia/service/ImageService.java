package com.alexwestcott.socialmedia.service;

import com.alexwestcott.socialmedia.domain.Image;
import com.alexwestcott.socialmedia.repo.ImageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
    }

    @Bean
    CommandLineRunner setUp() throws IOException {
        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-cover.jpg"));

            FileCopyUtils.copy("Test file2",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-2nd-edition-cover.jpg"));

            FileCopyUtils.copy("Test file3",
                    new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
        };
    }

    public Mono<Resource> findOneImage(String imageName){
        return Mono.fromSupplier(
                () -> resourceLoader.getResource(
                        "file:" + UPLOAD_ROOT + "/" + imageName
                )
        );
    }

    public Mono<Void> deleteImage(String imageName){

        Mono<Void> deleteDatabaseImage = imageRepository.findByName(imageName).flatMap(imageRepository::delete).log("Delete image - database");

        Mono<Object> deleteFile = Mono.fromRunnable(
                () -> {
                    try{
                        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, imageName));
                    } catch(IOException e){
                        throw new RuntimeException(e);
                    }
                }
        ).log("Delete image - file");

        return Mono.when(deleteDatabaseImage, deleteFile).log("Delete image - when").then().log("Delete image - done");
    }

    public Flux<Image> findAllImages(){
        return imageRepository.findAll();
    }

    public Mono<Void> createImage(Flux<FilePart> files){
        return files.flatMap(file -> {
            Mono<Image> saveDatabaseImage = imageRepository.save(new Image(UUID.randomUUID().toString(), file.filename())).log("Create image - save");
            Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile()).log("Create image - find path")
                    .map(destFile -> {
                        try{
                            destFile.createNewFile();
                            return destFile;
                        } catch(IOException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .log("Create image - new file")
                    .flatMap(file::transferTo)
                    .log("Create image - copy");
            return Mono.when(saveDatabaseImage, copyFile);
        }).then().log("Create image - done");
    }
}
