package com.paytm.hellopiper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.hellopiper.model.WeatherInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    @Value("${weatherinfo.url}")
    private String baseUrl;

    @Value("${weatherinfo.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherInfo weather(String city) {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse httpResponse = client.execute(new HttpGet(String.format(baseUrl, city, apiKey)));
            WeatherInfo weatherInfo = objectMapper.readValue(httpResponse.getEntity().getContent(), WeatherInfo.class);
            return weatherInfo;
        } catch (IOException e) {
            throw new IllegalStateException("Exception occured while getting weather!!!", e);
        }
    }
}
