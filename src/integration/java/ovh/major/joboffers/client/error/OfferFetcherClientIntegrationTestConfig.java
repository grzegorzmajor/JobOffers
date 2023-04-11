package ovh.major.joboffers.client.error;

import org.springframework.web.client.RestTemplate;
import ovh.major.joboffers.domain.offer.OfferFetchable;
import ovh.major.joboffers.infrastructure.offer.fetcher.OfferFetcherClientConfig;

import static ovh.major.joboffers.BaseIntegrationTest.WIRE_MOCK_HOST;

public class OfferFetcherClientIntegrationTestConfig extends OfferFetcherClientConfig {

    public OfferFetchable remoteOfferClient(int port, int connectionTimeout, int readTimeout) {
        RestTemplate restTemplate = restTemplate(connectionTimeout,readTimeout, restTemplateResponseErrorHandler());
        return remoteOfferClient(restTemplate, WIRE_MOCK_HOST, port);
    }

}
