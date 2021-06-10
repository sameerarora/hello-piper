package com.paytm.hellopiper.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.paytm.hellopiper.model.WeatherInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FeatureSteps extends SprintIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private ResponseEntity<String> responseEntity;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Given("^The dependencies are up and running$")
    public void boot_up() {
        wireMockServer = new WireMockServer(wireMockConfig().port(port));
        wireMockServer.start();
        configureFor(port);
    }

    @And("^Response from Weather service is set up for (.+)$")
    public void dependencies_are_up_and_running(String cityName) throws IOException {
        wireMockServer.addStubMapping(stubFor(get(urlEqualTo("/mock/weather/" + cityName + "/dummy"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(setExpectedResponse(cityName)))));
    }

    @When("^the user fetches the weather information for (.+)$")
    public void get_weather_for_city(String city) {
        responseEntity = restTemplate.exchange(String.format(apiEndpoint, applicationPort, city), HttpMethod.GET, null, String.class);
    }

    @Then("^Verify response is valid and contains weather information for (.+) with min temp (.+) and max temp (.+)$")
    public void verify_response(String city, String minimumTemp, String maximumTemp) throws IOException {
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
//        WeatherInfo weatherInfo = objectMapper.readValue(responseEntity.getBody(), WeatherInfo.class);
//        String responseJson =  objectMapper.writeValueAsString(weatherInfo).trim();
        WeatherInfo weatherInfo = objectMapper.readValue(responseEntity.getBody(), WeatherInfo.class);
        assertThat(weatherInfo.getCityName(), is(city));
        assertThat(weatherInfo.getTemperatureInfo().getTempMin(), is(minimumTemp));
        assertThat(weatherInfo.getTemperatureInfo().getTempMax(), is(maximumTemp));

        //assertThat(responseJson, is(loadExpectedResponse(city.toLowerCase())));
    }

    @And("^dependencies are shut down successfully$")
    public void tear_down() {
        wireMockServer.stop();
    }

}
