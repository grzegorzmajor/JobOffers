package ovh.major.joboffers.domain.offer.exceptions;

public class DuplicateOfferKeyException extends RuntimeException {
    public DuplicateOfferKeyException(ExceptionMessages message) {
        super(message.toString());
    }
}
