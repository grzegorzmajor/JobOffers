package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.dto.OfferRequestDto;
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

    SavingOfferResultDto saveOffer(OfferRequestDto offerDto){
        final Offer offer = Offer.builder()
                .position(offerDto.position())
                .company(offerDto.company())
                .salary(offerDto.salary())
                .offerUrl(offerDto.offerUrl())
                .build();
        Offer savedOffer = offerRepository.save(offer);
        return new SavingOfferResultDto(
                savedOffer.id(),
                savedOffer.position(),
                savedOffer.company(),
                savedOffer.salary(),
                savedOffer.offerUrl(),
                true
        );
    }

    void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    void deleteOfferByUrl(String url) {
        offerRepository.deleteByUrl(url);
    }

}
