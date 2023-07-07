package pl.lingwenta.recruitment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import pl.lingwenta.recruitment.api.Request;
import pl.lingwenta.recruitment.repo.RequestRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

public class RecruitmentApplicationTests extends IntegrationTestBase {

    private static final String CONTROLLER_URI = "/weather";
    private static final String LONGITUDE = "37.00010";
    private static final String LATITUDE = "17.01101";

    private static final String OM_API_ARCHIVE_REQ_URI = "/v1/archive";

    @Value("${openmeteo-client.api.daily-properties}")
    private String dailyProperties;
    @Value("${openmeteo-client.api.timezone}")
    private String timezone;

    @Autowired
    RequestRepository requestRepository;

    @Test
    void applicationHappyPathTestLastWeekWeather() throws IOException {
        //given
        server.stubFor(get(urlPathMatching(OM_API_ARCHIVE_REQ_URI))
                .willReturn(ok()
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(getJsonFile("openmeteoResponse.json"))));

        //when then
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CONTROLLER_URI)
                        .pathSegment(LONGITUDE, LATITUDE)
                        .build())
                .header(ACCEPT,APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(getJsonFile("happyPathResponse.json"));

        assertThat(requestRepository.findAll().get(0))
                .hasFieldOrPropertyWithValue("latitude", LATITUDE)
                .hasFieldOrPropertyWithValue("longitude", LONGITUDE);
    }

    @Test
    void applicationUnhappyPathTestInvalidAcceptHeader() {
        //when then
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CONTROLLER_URI)
                        .pathSegment(LONGITUDE, LATITUDE)
                        .build())
                .header(ACCEPT, APPLICATION_XML_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(406)
                .expectHeader()
                .contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo("406 NOT_ACCEPTABLE")
                .jsonPath("$.message").isEqualTo("No acceptable representation");
    }
}
