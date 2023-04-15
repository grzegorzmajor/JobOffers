package ovh.major.joboffers.infrastructure.offer.fetcher;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Log4j2
class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        final HttpStatusCode statusCode = httpResponse.getStatusCode();
        log.error("Rest Template Error Handler - status code: " + statusCode.value());
        if (statusCode.is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while using http client");
        } else if (statusCode.is4xxClientError()) {
            if (statusCode.value() == HttpStatus.NOT_FOUND.value()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else if (statusCode.value() == HttpStatus.UNAUTHORIZED.value()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }

}
