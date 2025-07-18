package com.tus.traunreut;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractScraper<T> implements IScraper<T> {
    private static final Logger networkLogger = LoggerFactory.getLogger("NETWORK");

    protected String getRequest(String url) {
        networkLogger.info(Markers.NETWORK,"GET request to {}", url);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/json");

            return client.execute(request, response -> {
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new IOException("Unexpected response status: " + statusCode);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
