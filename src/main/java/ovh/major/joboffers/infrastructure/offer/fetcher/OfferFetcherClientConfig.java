package ovh.major.joboffers.infrastructure.offer.fetcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ovh.major.joboffers.domain.offer.OfferFetchable;
import java.time.Duration;

@Configuration
public class OfferFetcherClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(@Value("${offer.fetcher.client.config.connectionTimeout}") long connectionTimeout,
                              @Value("${offer.fetcher.client.config.readTimeout}") long readTimeout,
                              RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferClient(RestTemplate restTemplate,
                                            @Value("${offer.fetcher.client.config.uri}") String uri,
                                            @Value("${offer.fetcher.client.config.port}") int port ){
        return new OfferFetcherClient(restTemplate, uri, port);
    }
}
