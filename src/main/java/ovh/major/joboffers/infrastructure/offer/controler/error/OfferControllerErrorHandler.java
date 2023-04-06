package ovh.major.joboffers.infrastructure.offer.controler.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

@ControllerAdvice
@Log4j2
public class OfferControllerErrorHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferControllerErrorResponse offerNotFound(OfferNotFoundException exception){
        String message = exception.getMessage();
        log.error("OfferControllerErrorHandler: " + message);
        return new OfferControllerErrorResponse(message, HttpStatus.NOT_FOUND);
    }
}
