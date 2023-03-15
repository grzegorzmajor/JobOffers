package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.OFFER_NOT_FOUND;

public class OfferFacade {

    private final OfferRepository offerRepository;

    public OfferFacade(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    List<OfferDto> findAllOffers() {
        return null;
    }

    OfferDto findOfferById(String id){
        return offerRepository.findOfferById(id)
                .map(offer -> new OfferDto(offer.title(),offer.company(),offer.salary(),offer.offerUrl()))
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
    }

    void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    void deleteOfferById(String id) {
        offerRepository.deleteOfferById(id);
    }


}
