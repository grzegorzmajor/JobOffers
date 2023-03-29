package ovh.major.joboffers.domain.offer.exceptions;

public class DuplicateOfferException extends RuntimeException {
    public DuplicateOfferException(ExceptionMessages message) {
        super(message.toString());
    }
}
