package pl.lingwenta.recruitment.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public WebClient openMeteoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiUrl)
                .build();
    }

    @Bean
    public OpenMeteoArchiveClient openMeteoArchiveClient(WebClient webClient) {
        return new OpenMeteoArchiveClient(webClient, dailyProperties, timezone);
    }
}
