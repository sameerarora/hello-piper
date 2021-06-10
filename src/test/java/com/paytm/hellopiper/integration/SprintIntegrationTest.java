package com.paytm.hellopiper.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.*;

import static java.lang.String.format;

@CucumberContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "classpath:application-test.properties")
@TestPropertySource(locations = "classpath:application-test.properties")
public class SprintIntegrationTest {

    @Value("${wiremock.port}")
    protected int port;

    @Value("${weatherinfo.url}")
    String weatherInfoUrl;

    @Value("${api.endpoint}")
    String apiEndpoint;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    @LocalServerPort
    protected int applicationPort;

    WireMockServer wireMockServer;

    protected final String setExpectedResponse(String cityName) throws IOException {
        InputStream inputStream = new FileInputStream(ResourceUtils.getFile(this.getClass().getResource(format("/scenarios/input/%s.json", cityName))));
        return readFromInputStream(inputStream);
    }

    protected final String loadExpectedResponse(String cityName) throws IOException {
        InputStream inputStream = new FileInputStream(ResourceUtils.getFile(this.getClass().getResource(format("/scenarios/output/%s.json", cityName))));
        return readFromInputStream(inputStream).trim();
    }

    private final String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
