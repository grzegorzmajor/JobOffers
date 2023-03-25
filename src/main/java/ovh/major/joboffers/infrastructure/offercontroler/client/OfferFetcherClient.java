package ovh.major.joboffers.infrastructure.offercontroler.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ovh.major.joboffers.domain.offer.OfferFetchable;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferFetcherClient implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    private final String parameter;

    @Override
    public List<OfferExternalResponseDto> fetchOffers() {
       log.info("Started fetching offers using http client");
       HttpHeaders headers = new HttpHeaders();
       final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
       try {
           String urlForService = getUrl();
           final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
           ResponseEntity<List<OfferExternalResponseDto>> response = restTemplate.exchange(
                   url,
                   HttpMethod.GET,
                   requestEntity,
                   new ParameterizedTypeReference<>(){
                   });
           final List<OfferExternalResponseDto> body = response.getBody();
           if (body == null) {
               log.info("Response body was null! Returning empty list");
               return Collections.emptyList();
               }
           log.info("Success! Response body returned size is " + body.size());
           return body;
       } catch (ResourceAccessException exception) {
           log.error("Error while fetching offers using http client: " +  exception.getMessage());
           return Collections.emptyList();
       }
    }

    private String getUrl() {
        return uri + ":" + port + parameter;
    }
}
