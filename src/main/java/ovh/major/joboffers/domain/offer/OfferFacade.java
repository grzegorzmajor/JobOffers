package ovh.major.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;
import java.util.List;
import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.OFFER_NOT_FOUND;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;

    private final OfferService offerService;


    public List<OfferDBResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        return offerService.fetchAllOffersAndSaveAllIfNotExists()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .toList();
    }

    public List<OfferDBResponseDto> findAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(offer -> new OfferDBResponseDto(
                        offer.id(),
                        offer.position(),
                        offer.company(),
                        offer.salary(),
                        offer.offerUrl()))
                .toList();
    }

    public OfferDBResponseDto findOfferByUrl(String url){
        return offerRepository.findByOfferUrl(url)
                .map(offer -> new OfferDBResponseDto(
                        offer.id(),
                        offer.position(),
                        offer.company(),
                        offer.salary(),
                        offer.offerUrl()))
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
    }

    public OfferDBResponseDto findOfferById(String id){
        return offerRepository.findById(id)
                .map(offer -> new OfferDBResponseDto(
                        offer.id(),
                        offer.position(),
                        offer.company(),
                        offer.salary(),
                        offer.offerUrl()))
                .orElseThrow(() -> new OfferNotFoundException(OFFER_NOT_FOUND));
    }

    public OfferDBResponseDto saveOffer(OfferDBRequestDto offerDto){
        final Offer offer = OfferMapper.mapFromOfferRequestToOffer(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        return OfferMapper.mapFromOfferToOfferDto(savedOffer);
    }

    void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    void deleteOfferByUrl(String url) {
        offerRepository.deleteByOfferUrl(url);
    }

}
