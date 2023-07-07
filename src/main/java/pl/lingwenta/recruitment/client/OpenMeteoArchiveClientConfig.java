package pl.lingwenta.recruitment.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenMeteoArchiveClientConfig {

    @Value("${openmeteo-client.api.timezone}")
    private String timezone;
    @Value("${openmeteo-client.api.daily-properties}")
    private String dailyProperties;
    @Value("${openmeteo-client.api.url}")
    private String apiUrl;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper);
    }

    @Bean
    public WebClient openmeteoWebClient(Jackson2JsonDecoder decoder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(decoder))
                .build();

        return WebClient.builder()
                .baseUrl(apiUrl)
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public OpenMeteoArchiveClient openMeteoArchiveClient(WebClient webClient) {
        return new OpenMeteoArchiveClient(webClient, dailyProperties, timezone);
    }
}
