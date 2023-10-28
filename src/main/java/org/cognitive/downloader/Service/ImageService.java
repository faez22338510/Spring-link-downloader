package org.cognitive.downloader.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class ImageService {

//    @Value("${image.save.path}") // Define a property in your application.properties or application.yml
//    private String imageSavePath;

    public Mono<Void> saveImage(String imageName, byte[] imageData) {
        String imageSavePath = "";
        Path imagePath = Path.of(imageSavePath, imageName);
        return Mono.fromCallable(() -> {
            Files.write(imagePath, imageData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return null;
        });
    }
}