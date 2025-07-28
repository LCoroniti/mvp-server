package com.tus.traunreut;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

import static com.tus.traunreut.Log.NETWORK;
import static com.tus.traunreut.Log.NETWORK_LOG;

public abstract class AbstractScraper<T> implements IScraper<T> {

    protected String getRequest(String url) throws IOException {
        NETWORK_LOG.info(NETWORK, "GET request to {}", url);
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
        }
    }
}
