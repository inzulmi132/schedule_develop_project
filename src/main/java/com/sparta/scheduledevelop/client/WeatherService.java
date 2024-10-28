package com.sparta.scheduledevelop.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;

    public String getWeather() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        return weatherClient.getWeather()
                .stream()
                .filter(weatherResponse -> Objects.equals(weatherResponse.getDate(), today))
                .map(WeatherResponse::getWeather)
                .findFirst()
                .orElse(null);
    }
}
