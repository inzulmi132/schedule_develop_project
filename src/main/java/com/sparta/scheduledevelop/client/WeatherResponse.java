package com.sparta.scheduledevelop.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private String date;
    private String weather;
}
