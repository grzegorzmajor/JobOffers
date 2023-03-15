package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.dto.SavingOfferResultDto;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.OFFER_NOT_FOUND;

public class OfferFacade {

    private final OfferRepository offerRepository;

    public OfferFacade(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    List<OfferDto> findAllOffers() {
        List<Offer> offers = offerRepository.findAllOffers()
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
        return offers.stream()
                .map(offer -> new OfferDto(offer.title(),offer.company(),offer.salary(),offer.offerUrl()))
                .toList();
    }

    OfferDto findOfferById(String id){
        return offerRepository.findOfferById(id)
                .map(offer -> new OfferDto(offer.title(),offer.company(),offer.salary(),offer.offerUrl()))
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
    }

    SavingOfferResultDto saveOffer(OfferDto offerDto){
        final Offer offer = Offer.builder()
                .title(offerDto.title())
                .company(offerDto.company())
                .salary(offerDto.salary())
                .offerUrl(offerDto.offerUrl())
                .build();
        Offer savedOffer = offerRepository.save(offer);
        return new SavingOfferResultDto(
                savedOffer.title(),
                savedOffer.company(),
                savedOffer.salary(),
                savedOffer.offerUrl(),
                true
        );
    }

    void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    void deleteOfferById(String id) {
        offerRepository.deleteOfferById(id);
    }

}
