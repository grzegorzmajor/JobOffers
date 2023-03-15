package ovh.major.joboffers.domain.offer.exceptions;

public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(ExceptionMessages message) {
        super(message.toString());
    }
}
