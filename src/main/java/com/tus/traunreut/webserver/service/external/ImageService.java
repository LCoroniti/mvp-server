package com.tus.traunreut.webserver.service.external;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
public class ImageService {
    /**
     * Fetches the logo from the given URL and returns it as a Base64 encoded string.
     */
    @Transactional
    public String fetchLogoBase64(String url) throws Exception {
        URL imageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Open the connection to the URL
        connection.connect();

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read data from InputStream into byte buffer and write to ByteArrayOutputStream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convert ByteArrayOutputStream to byte array
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
    }
}
