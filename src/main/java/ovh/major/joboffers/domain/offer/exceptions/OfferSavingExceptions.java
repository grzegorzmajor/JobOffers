package ovh.major.joboffers.domain.offer.exceptions;

public class OfferSavingExceptions extends RuntimeException {

    public OfferSavingExceptions(ExceptionMessages message) {
        super(message.toString());
    }
}
