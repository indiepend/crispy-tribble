package pl.lingwenta.recruitment;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes =  {
        IntegrationTestBase.IntegrationTestConfiguration.class
})
public class IntegrationTestBase {

    @Autowired
    protected WireMockServer server;

    @Autowired
    protected WebTestClient client;

    protected String getJsonFile(String filename) throws IOException {
        String fileUri = getClass().getClassLoader().getResource(filename).getFile();
        FileInputStream fileInputStream = new FileInputStream(fileUri);
        return new String(fileInputStream.readAllBytes());
    }

    @TestConfiguration
    static class IntegrationTestConfiguration {

        @Bean(initMethod = "start", destroyMethod = "stop")
        public WireMockServer wiremockServer() {
            return new WireMockServer(options().dynamicPort());
        }

        @Bean
        @Primary
        public WebClient testWebClient(WebClient webClient, WireMockServer wireMockServer) {
            return webClient.mutate()
                    .baseUrl(wireMockServer.baseUrl())
                    .build();
        }

    }
}
