package com.tus.traunreut.webserver.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<ByteArrayResource> getImage(@RequestParam String imageUrl) {
        try {
            // Make an HTTP GET request to fetch the image
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    null,
                    byte[].class
            );

            // Check if the response is valid
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Create ByteArrayResource from image bytes
                ByteArrayResource resource = new ByteArrayResource(response.getBody());

                // Return image data with appropriate headers
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Change to correct image type if needed
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=image.jpg")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
