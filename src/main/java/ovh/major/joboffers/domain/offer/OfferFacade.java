package ovh.major.joboffers.domain.offer;

public class OfferFacade {

    private final OfferRepository offerRepository;

    public OfferFacade(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }


}
