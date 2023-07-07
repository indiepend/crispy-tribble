package pl.lingwenta.recruitment.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lingwenta.recruitment.api.DayWeatherDto;
import pl.lingwenta.recruitment.api.Request;
import pl.lingwenta.recruitment.client.OpenMeteoArchiveClient;
import pl.lingwenta.recruitment.repo.RequestRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static pl.lingwenta.recruitment.api.DayWeatherDto.convertToList;

public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final OpenMeteoArchiveClient client;
    private final RequestRepository requestRepository;
    private final Clock clock;

    public WeatherService(OpenMeteoArchiveClient client, RequestRepository requestRepository, Clock clock) {
        this.client = client;
        this.requestRepository = requestRepository;
        this.clock = clock;
    }

    public List<DayWeatherDto> getLastWeekWeatherInformation(String longitude, String latitude) {
        log.info("Saving request to database");
        requestRepository.save(prepareRequestEntity(longitude, latitude));

        log.info("Retrieving last week weather information for longitude: [{}], latitude [{}]", longitude, latitude);
        return convertToList(client
                .getWeatherInfoFomGivenTimeWindow(longitude, latitude,
                        LocalDate.now(clock).minusDays(7),
                        LocalDate.now(clock).minusDays(1))
                .block()
        );
    }

    private Request prepareRequestEntity(String longitude, String latitude) {
        return new Request(latitude, longitude, LocalDateTime.now(clock));
    }
}
