package pl.lingwenta.recruitment.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DailyInfo (List<LocalDate> dates, List<LocalDateTime> sunrises, List<LocalDateTime> sunsets, List<Float> rainSums){

    @JsonCreator
    public DailyInfo(@JsonProperty("time") List<LocalDate> dates,
                     @JsonProperty("sunrise") List<LocalDateTime> sunrises,
                     @JsonProperty("sunset") List<LocalDateTime> sunsets,
                     @JsonProperty("rain_sum") List<Float> rainSums) {
        this.dates = dates;
        this.sunrises = sunrises;
        this.sunsets = sunsets;
        this.rainSums = rainSums;
    }
}
