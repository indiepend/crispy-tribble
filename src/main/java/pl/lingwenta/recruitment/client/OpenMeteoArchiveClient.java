package pl.lingwenta.recruitment.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lingwenta.recruitment.client.api.ClientException;
import pl.lingwenta.recruitment.client.api.WeatherDailyInfo;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OpenMeteoArchiveClient {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoArchiveClient.class);
    private static final String ARCHIVE_URI = "/v1/archive";

    private final String dailyProperties;
    private final String timezone;
    private final WebClient webClient;

    public OpenMeteoArchiveClient(WebClient webClient, String dailyProperties, String timezone) {
        this.webClient = webClient;
        this.dailyProperties = dailyProperties;
        this.timezone = timezone;
    }

    public Mono<WeatherDailyInfo> getWeatherInfoFomGivenTimeWindow(String longitude, String latitude, LocalDate startDate, LocalDate endDate) {
        log.info("Sending request for daily weather info for longitude {} and latitude {} from start date {} until end date {}",
                longitude, latitude, startDate, endDate);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ARCHIVE_URI)
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("daily", dailyProperties)
                        .queryParam("start_date", startDate.format(DateTimeFormatter.ISO_DATE))
                        .queryParam("end_date", endDate.format(DateTimeFormatter.ISO_DATE))
                        .queryParam("timezone", timezone)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(WeatherDailyInfo.class);
    }

    private Mono<RuntimeException> handleError(ClientResponse response) {
        log.warn("Encountered error response with code [{}]", response.statusCode());

        if (HttpStatus.valueOf(response.statusCode().value()) == HttpStatus.TOO_MANY_REQUESTS) {
            return Mono.error(ClientException.tooManyRequests());
        }
        return Mono.error(ClientException.unknownClientError(response.statusCode()));
    }
}
