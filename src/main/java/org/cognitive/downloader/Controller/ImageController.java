package org.cognitive.downloader.Controller;

import org.cognitive.downloader.Service.ImageDownloadService;
import org.cognitive.downloader.Service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@RestController
public class ImageController {

    private final ImageDownloadService imageDownloadService;

    private final ImageService imageService;
    private static final String IMAGE_DIRECTORY = "images/";

    public ImageController(ImageDownloadService imageDownloadService, ImageService imageService) {
        this.imageDownloadService = imageDownloadService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<Resource> getImage(
            @RequestParam String image
    ) {
        System.out.println(new Date());
        System.out.println(image);
        // Build the path to the image file
        String[] parts = image.split("/");
        String imageName = parts[parts.length - 1];
        Path imagePath = Paths.get("").resolve(IMAGE_DIRECTORY).resolve(imageName).toAbsolutePath();
//        imagePath = Paths.get(IMAGE_DIRECTORY).resolve(imageName);
        try {

            // Try to load the image
            Resource resource = new UrlResource(imagePath.toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", imageName);
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                var x = imageDownloadService.downloadImage(image).block();
                if (imageName.contains("."))
                    imageService.saveImage(IMAGE_DIRECTORY + imageName, x).block();
                else
                    imageService.saveImage(IMAGE_DIRECTORY + imageName + ".png", x).block();

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new UrlResource(imagePath.toUri()));
            }
        } catch (MalformedURLException e) {
            // Handle the exception
        }
        // Handle other errors
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/im")
    public ResponseEntity<Resource> getImageFromGermanServer(
            @RequestParam String image
    ) {
        System.out.println(new Date());
        System.out.println(image);
        // Build the path to the image file
        String[] parts = image.split("/");
        String imageName = parts[parts.length - 1];
        Path imagePath = Paths.get("").resolve(IMAGE_DIRECTORY).resolve(imageName).toAbsolutePath();
//        imagePath = Paths.get(IMAGE_DIRECTORY).resolve(imageName);
        try {

            // Try to load the image
            Resource resource = new UrlResource(imagePath.toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", imageName);
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
//                imageDownloadService.downloadImage("http://85.185.67.243:8080/images?image=" + image,IMAGE_DIRECTORY + imageName);
                imageDownloadService.downloadImage("http://49.13.30.62:8081?image=" + image,IMAGE_DIRECTORY + imageName);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new UrlResource(imagePath.toUri()));
            }
        } catch (MalformedURLException e) {
            // Handle the exception
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Handle other errors
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/test")
    public String test(
            @RequestParam String image
    ) {
        System.out.println(new Date());
        System.out.println(image);
        return image;
    }

}
