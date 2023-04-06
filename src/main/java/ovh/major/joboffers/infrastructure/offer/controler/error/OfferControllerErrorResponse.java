package ovh.major.joboffers.infrastructure.offer.controler.error;

import org.springframework.http.HttpStatus;

public record OfferControllerErrorResponse(
        String message,
        HttpStatus status){

}
