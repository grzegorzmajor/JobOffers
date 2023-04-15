package ovh.major.joboffers.infrastructure.offer.controler.error;

import org.springframework.http.HttpStatus;

import java.util.List;

record OfferPostErrorResponse (
        List<String> strings,
        HttpStatus httpStatus)
{
}
