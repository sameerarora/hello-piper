package com.paytm.hellopiper.controller;

import com.paytm.hellopiper.model.WeatherInfo;
import com.paytm.hellopiper.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@PropertySource("classpath:application.properties")
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/{city}")
    public WeatherInfo weather(@PathVariable String city) {
        return weatherService.weather(city);
    }

}
