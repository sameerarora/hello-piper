package com.paytm.hellopiper;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.hellopiper.model.TemperatureInfo;
import com.paytm.hellopiper.model.WeatherInfo;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SampleTest {

    ObjectMapper objectMapper = new ObjectMapper();

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
    public void jwt() throws UnsupportedEncodingException {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+05:30")).plusMinutes(2);
        String ts = localDateTime.toString();
        Algorithm buildAlgorithm = Algorithm.HMAC256("083e1fa8-f144-4d62-af65-a8728e3e132e");
        String token = JWT.create().withIssuer("OE")
                .withClaim("clientId", "RISK")
                .withClaim("custId", "1000704643")
                .withClaim("timestamp", ts + "+05:30").sign(buildAlgorithm);
        System.out.println(token);
    }
}
