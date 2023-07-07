package pl.lingwenta.recruitment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.lingwenta.recruitment.client.OpenMeteoArchiveClient;
import pl.lingwenta.recruitment.client.OpenMeteoArchiveClientConfig;
import pl.lingwenta.recruitment.repo.RequestRepository;
import pl.lingwenta.recruitment.web.CustomWebConfigurer;
import pl.lingwenta.recruitment.web.CustomWebHandlerExceptionResolver;
import pl.lingwenta.recruitment.web.WeatherService;

import java.time.Clock;

@Configuration
@Import(OpenMeteoArchiveClientConfig.class)
@EnableJpaRepositories
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public WeatherService weatherService(OpenMeteoArchiveClient openMeteoArchiveClient,
                                         RequestRepository requestRepository,
                                         Clock clock) {
        return new WeatherService(openMeteoArchiveClient, requestRepository, clock);
    }

    @Bean
    public CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver(ObjectMapper objectMapper) {
        return new CustomWebHandlerExceptionResolver(objectMapper);
    }

    @Bean
    public CustomWebConfigurer customWebConfigurer(CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver) {
        return new CustomWebConfigurer(customWebHandlerExceptionResolver);
    }
}
