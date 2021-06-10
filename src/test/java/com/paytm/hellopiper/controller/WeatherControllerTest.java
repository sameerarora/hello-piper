package com.paytm.hellopiper.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    @Value("${api.endpoint}")
    private String apiEndpoint;

    @LocalServerPort
    private int applicationPort;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    TestRestTemplate restTemplate = new TestRestTemplate();

    WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().port(port));
        wireMockServer.start();
    }

    @After
    public void tearDown() {
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
        ResponseEntity<String> responseEntity = restTemplate.exchange(String.format(apiEndpoint, applicationPort, "chennai"), HttpMethod.GET, null, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherInfo weatherInfo = objectMapper.readValue(responseEntity.getBody(), WeatherInfo.class);
        assertThat(weatherInfo.getCityName(), is("Chennai"));
    }

    @Test
    public void jwt() throws UnsupportedEncodingException {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+05:30")).plusMinutes(2);
        String ts = localDateTime.toString();
        Algorithm buildAlgorithm = Algorithm.HMAC256("083e1fa8-f144-4d62-af65-a8728e3e132e");
        String token = JWT.create().withIssuer("OE")
                .withClaim("clientId", "RISK")
                .withClaim("custId", "1000704643")
                .withClaim("timestamp", ts + "+05:30").sign(buildAlgorithm);
        System.out.println(token);

//        Algorithm algorithm = Algorithm.HMAC256("6960f1ac-c364-11eb-a122-f0189877b474");
//        String jwtToken = JWT.create()
//                .withIssuer("paytm-onboarding-engine")
//                .withClaim("X-Client-Secret", "6960f1ac-c364-11eb-a122-f0189877b474")
//                .withClaim("payload","{\n" +
//                        "  \"lead\": {\n" +
//                        "    \"id\": \"550a9490-89c5-4498-ba46-3de5f877f07\",\n" +
//                        "    \"creation_date\": \"23-08-1996\" \n" +
//                        "  },\n" +
//                        "  \"user\": {\n" +
//                        "    \"last_name\": \"SINGH\", \n" +
//                        "    \"first_name\": \"USHA\", \n" +
//                        "    \"middle_name\": \"K\",  \n" +
//                        "    \"id\":\"1000704643\", \n" +
//                        "    \"dob\": \"14-03-1972\", \n" +
//                        "    \"pan\": \"BKLPS9641B\", \n" +
//                        "    \"email\": \"yogesh.yadav@paytm.com\",\n" +
//                        "    \"mobile\": \"7411000282\", \n" +
//                        "    \"location\" : {\n" +
//                        "      \"latitude\": null, \n" +
//                        "      \"longitude\": null\n" +
//                        "    },\n" +
//                        "    \"product\": {\n" +
//                        "      \"type\": \"PP\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}")
//              .sign(algorithm);
//        System.out.println(jwtToken);
    }
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJZCI6IlJJU0siLCJpc3MiOiJSSVNLIiwiY3VzdElkIjoiMTAwMDcwNDY0MyIsInRpbWVzdGFtcCI6IkZyaSBKdW4gMDQgMTc6MTg6MDEgSVNUIDIwMjErMDU6MzAifQ.CK48LsCXAIKx2i0ag_Rv8HbAW3klGwgG_jWvxi4jyCs
}