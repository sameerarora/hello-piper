package com.paytm.hellopiper.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.paytm.hellopiper.model.WeatherInfo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "classpath:application-test.properties")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WeatherControllerTest {

    @Value("${wiremock.port}")
    private int port;

    @LocalServerPort
    private int applicationPort;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    TestRestTemplate restTemplate = new TestRestTemplate();

    WireMockServer wireMockServer;

    @Before
    public void setUp() throws Exception {
        wireMockServer = new WireMockServer(wireMockConfig().port(port));
        wireMockServer.start();
    }

    @After
    public void tearDown() throws Exception {
        wireMockServer.stop();
    }

    @Test
    public void shouldReturnWeatherInfoForCityRequested() throws IOException {
        String responseBody = "{\"coord\":{\"lon\":80.2785,\"lat\":13.0878},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":32,\"feels_like\":36.48,\"temp_min\":32,\"temp_max\":32,\"pressure\":1003,\"humidity\":58},\"visibility\":6000,\"wind\":{\"speed\":3.09,\"deg\":190},\"clouds\":{\"all\":20},\"dt\":1617371283,\"sys\":{\"type\":1,\"id\":9218,\"country\":\"IN\",\"sunrise\":1617323659,\"sunset\":1617367820},\"timezone\":19800,\"id\":1264527,\"name\":\"Chennai\",\"cod\":200}";
        wireMockServer.addStubMapping(stubFor(get(urlEqualTo("/mock/weather/chennai/dummy"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody))));
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + applicationPort + "/weather/chennai", HttpMethod.GET, null, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherInfo weatherInfo = objectMapper.readValue(responseEntity.getBody(), WeatherInfo.class);
        assertThat(weatherInfo.getCityName(), is("Chennai"));
    }


}