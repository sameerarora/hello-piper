package com.paytm.hellopiper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.hellopiper.model.TemperatureInfo;
import com.paytm.hellopiper.model.WeatherInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SampleTest {

    ObjectMapper objectMapper = new ObjectMapper();
    private String payload;

    @Before
    public void setUp() throws Exception {
        payload = "{\n" +
                "  \"lead\": {\n" +
                "    \"id\": \"734fff57-e801-4942-b21e-42b04174bc0f\",\n" +
                "    \"creation_date\": \"14-06-2021\"\n" +
                "  },\n" +
                "  \"user\": {\n" +
                "    \"last_name\": \"Limit\",\n" +
                "    \"first_name\": \"TO\",\n" +
                "    \"middle_name\": \"WOOD\",\n" +
                "    \"id\": \"1001541553\",\n" +
                "    \"dob\": \"1988-12-18\",\n" +
                "    \"pan\": \"GDKPG5263M\",\n" +
                "    \"email\": \"abcd@gmail.com\",\n" +
                "    \"location\": {\n" +
                "      \"longitude\": \"1.0\",\n" +
                "      \"latitude\": \"1.0\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"product\": {\n" +
                "    \"type\": \"PP\"\n" +
                "  }\n" +
                "}";
    }

    @Test
    public void testToJson() throws JsonProcessingException {
        String json = "{\"workflowOperation\":\"OFFER_STAGE1_SUCCESS\",\"solutionAdditionalInfo\":{\"OFFER_DETAILS\":\"{\"bureauData\":{\"creditScore\":722},\"offers\":[{\"customerId\":\"1000494469\",\"introductoryOfferText\":[\"2% convenience fee\",\"instant disbursal\"],\"lenderInfo\":{\"lender\":13,\"lenderCin\":\"1234\",\"lenderLogo\":\"https://lms-static.paytm.com/lending/images/brand/lender_logo_ABFL.jpg\",\"lenderName\":\"Aditya Birla Finance Limited\"},\"maxAmount\":20000.0,\"minAmount\":10000.0,\"offerEndDate\":1625077800000,\"offerId\":\"024d34eb-8512-43b1-8179-2eb1473aa175\",\"offerStartDate\":1622485800000,\"processingFeePercentage\":2.0,\"processingFeeText\":\"2.0% convenience fee to be charged\",\"productId\":\"67\",\"productType\":\"pp\",\"productVariant\":\"Delite\",\"productVersion\":\"1\",\"sourceOfWhitelist\":\"Risk\"}],\"reasons\":[],\"status\":\"SUCCESS\",\"transactionId\":\"8eb22744-d70b-4c3f-b1c1-97f9684daec0\"}\",\"OFFER_TIMESTAMP\":\"1623168247067\"}}";
        TemperatureInfo temperatureInfo = new TemperatureInfo();
        temperatureInfo.setTempMax("21.0");
        temperatureInfo.setTempMin("11.0");
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setTemperatureInfo(temperatureInfo);
        weatherInfo.setCityName("Gurugram");
        System.out.println(String.format("OFFER_STAGE%d_%s", 1, "FAILURE"));
        System.out.println(json.replaceAll("\\\"", "\""));
        Map<String, String> map = objectMapper.convertValue(weatherInfo,Map.class);
        System.out.println(map);

    }

    @Test
    public void forOE() throws UnsupportedEncodingException {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+05:30"));//.plusMinutes(2);
        String ts = localDateTime.toString();
        Algorithm buildAlgorithm = Algorithm.HMAC256("083e1fa8-f144-4d62-af65-a8728e3e132e");
        String token = JWT.create().withIssuer("OE")
                .withClaim("clientId", "RISK")
                .withClaim("custId", "1000698414")
                .withClaim("timestamp", ts + "+05:30").sign(buildAlgorithm);
        System.out.println(token);
    }

    public String fromOE() throws UnsupportedEncodingException {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+05:30")).plusMinutes(2);
        String ts = localDateTime.toString();
        Algorithm buildAlgorithm = Algorithm.HMAC256("6960f1ac-c364-11eb-a122-f0189877b474");
        String token = JWT.create().withIssuer("RISK")
                .withClaim("clientId", "paytm-onboarding-engine")
                .withClaim("payload", payload)
                .withClaim("timestamp", ts + "+05:30").sign(buildAlgorithm);
        System.out.println(token);
        return token;
    }

    @Test
    public void decode() throws UnsupportedEncodingException {
        String token = fromOE();
        DecodedJWT decode = JWT.decode(token);
        Map<String, Claim> claims = decode.getClaims();
        Algorithm buildAlgorithm;
        try {
            buildAlgorithm = Algorithm.HMAC256("6960f1ac-c364-11eb-a122-f0189877b474");
            String timestamp = claims.get("timestamp").asString();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime parse = LocalDateTime.parse(timestamp.replace("+05:30", ""), dateTimeFormatter);
            String generatedToken = JWT.create().withIssuer("RISK")
                    .withClaim("clientId", "paytm-onboarding-engine")
                    .withClaim("payload", claims.get("payload").asString())
                    .withClaim("timestamp", parse.toString() + "+05:30").sign(buildAlgorithm);
            //System.out.println(token);
            assertThat(token, is(generatedToken));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sample() {
        
    }
}
