package pl.lingwenta.recruitment.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lingwenta.recruitment.api.DayWeatherDto;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(path = "/{longitude}/{latitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DayWeatherDto> lastWeekWeather(@PathVariable String longitude, @PathVariable String latitude) {
        return weatherService.getLastWeekWeatherInformation(longitude, latitude);
    }
}
