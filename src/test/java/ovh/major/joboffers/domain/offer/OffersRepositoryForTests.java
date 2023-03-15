package ovh.major.joboffers.domain.offer;


import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.DUPLICATE_KEY;

public class OffersRepositoryForTests implements OfferRepository {

    List<Offer> offersList = new ArrayList<>();

    @Override
    public List<Offer> findAllOffers() {
        return offersList;
    }

    @Override
    public Optional<Offer> findOfferByUrl(String url) {
        return offersList.stream()
                .filter(offer -> url.equals(offer.offerUrl()))
                .findFirst();
    }

    @Override
    public Offer save(Offer offer) {
        final Optional<Offer> foundOffer = findOfferByUrl(offer.offerUrl());
        if( !foundOffer.isPresent() ) {
            offersList.add(offer);
        } else {
            throw new DuplicateOfferKeyException(DUPLICATE_KEY);
        }
        return offer;
    }

    @Override
    public boolean fetchAllOffersAndSaveAllIfNoExists() {
        return false;
    }

    @Override
    public void deleteAll() {
        offersList.clear();
    }

    @Override
    public void deleteOfferByUrl(String url) {
        offersList.remove(findOfferByUrl(url));
    }

    @Override
    public int size() {
        return offersList.size();
    }
}
