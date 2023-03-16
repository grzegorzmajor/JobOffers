package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.dto.OfferRequestDto;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.OFFER_NOT_FOUND;

public class OfferFacade {

    private final OfferRepository offerRepository;

    public OfferFacade(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    List<OfferDto> findAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(offer -> new OfferDto(
                        offer.id(),
                        offer.position(),
                        offer.company(),
                        offer.salary(),
                        offer.offerUrl()))
                .toList();
    }

    OfferDto findOfferByUrl(String url){
        return offerRepository.findByUrl(url)
                .map(offer -> new OfferDto(
                        offer.id(),
                        offer.position(),
                        offer.company(),
                        offer.salary(),
                        offer.offerUrl()))
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
    }

    OfferDto saveOffer(OfferRequestDto offerDto){
        final Offer offer = OfferMapper.mapFromOfferRequestToOffer(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        return OfferMapper.mapFromOfferToOfferDto(offer);
    }

    void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    void deleteOfferByUrl(String url) {
        offerRepository.deleteByUrl(url);
    }

}
