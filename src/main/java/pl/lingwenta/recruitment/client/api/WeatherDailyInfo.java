package pl.lingwenta.recruitment.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record WeatherDailyInfo(DailyInfo dailyInfo) {

    @JsonCreator
    public WeatherDailyInfo(@JsonProperty("daily") DailyInfo dailyInfo) {
        this.dailyInfo = dailyInfo;
    }
}
