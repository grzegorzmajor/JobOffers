package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferKeyException;
import ovh.major.joboffers.domain.offer.exceptions.OfferSavingExceptions;

import java.util.List;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.DUPLICATED_OFFER;

public class OfferService {

    private final OfferFetchable offerFetcher;
    private final OfferRepository offerRepository;

    public OfferService(OfferFetchable offerFetcher, OfferRepository offerRepository) {
        this.offerFetcher = offerFetcher;
        this.offerRepository = offerRepository;
    }

    List<Offer> fetchAllOffersAndSaveAllIfNotExists() {
        List<Offer> offers = fetchOffers();
        final List<Offer> offersForSaving = filterNotExistingOffers(offers);
        try {
            return offerRepository.saveAll(offersForSaving);
        } catch (DuplicateOfferKeyException exception) {
            throw new OfferSavingExceptions(DUPLICATED_OFFER);
        }

    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromExternalResponseDtoToOffer)
                .toList();
    }

    private List<Offer> filterNotExistingOffers(List<Offer> submittedOffers) {
        return submittedOffers.stream()
                .filter(offer -> !offer.offerUrl().isEmpty())
                .filter(offer -> !offerRepository.existsByUrl(offer.offerUrl()))
                .toList();
    }
}
