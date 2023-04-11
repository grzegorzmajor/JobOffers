package ovh.major.joboffers.infrastructure.offer.fetcher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import ovh.major.joboffers.domain.offer.OfferFetchable;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferFetcherClient implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public List<OfferExternalResponseDto> fetchOffers() {
        log.info("Started fetching offers using http client");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            String urlForService = getUrl("/offers");
            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<OfferExternalResponseDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });
            final List<OfferExternalResponseDto> body = response.getBody();
            if (body == null) {
                log.info("Response body was null!");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }
            log.info("Success! Response body returned size is " + body.size());
            return body;
        } catch (ResourceAccessException exception) {
            log.error("Error while fetching offers using http client: " + exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException exception) {
            //dodane ze względu na to że test should_throw_exception_500_when_fault_random_data_then_close() sie wywałał
            log.error("Error while fetching offers using http client: " + exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUrl(String service) {
        return uri + ":" + port + service;
    }
}
