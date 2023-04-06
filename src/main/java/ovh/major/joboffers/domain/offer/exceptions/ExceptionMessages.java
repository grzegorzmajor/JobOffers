package ovh.major.joboffers.domain.offer.exceptions;

public enum ExceptionMessages {
    OFFER_NOT_FOUND("Offer not found"),
    DUPLICATE_KEY("Offer key duplicated!"),
    DUPLICATED_OFFER("Offer is exist in repository!");

    String message;

    ExceptionMessages(String s) {
        message = s;
    }

    @Override
    public String toString() {
        return message;
    }
}
