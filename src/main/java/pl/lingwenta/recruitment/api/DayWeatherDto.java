package pl.lingwenta.recruitment.api;

import pl.lingwenta.recruitment.client.api.DailyInfo;
import pl.lingwenta.recruitment.client.api.WeatherDailyInfo;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record DayWeatherDto (String date, String sunriseHour, String sunsetHour, Float rainSum) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static List<DayWeatherDto> convertToList(WeatherDailyInfo weatherDailyInfo) {
        DailyInfo dailyInfo = weatherDailyInfo.dailyInfo();
        int listSize = dailyInfo.dates().size();
        List<DayWeatherDto> dtos = new ArrayList<>(listSize);

        for (int i = 0; i < listSize; i++) {
            dtos.add(new DayWeatherDto(
                    dailyInfo.dates().get(i).format(DateTimeFormatter.ISO_DATE),
                    dailyInfo.sunrises().get(i).format(TIME_FORMATTER),
                    dailyInfo.sunsets().get(i).format(TIME_FORMATTER),
                    dailyInfo.rainSums().get(i)
            ));
        }
        return dtos;
    }
}
