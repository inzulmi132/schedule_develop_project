package com.sparta.scheduledevelop.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "weatherInfo", url = "https://f-api.github.io/f-api/weather.json")
public interface WeatherClient {
    @GetMapping
    List<WeatherResponse> getWeather();
}
