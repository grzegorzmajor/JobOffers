package ovh.major.joboffers.domain.offer;


import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.DUPLICATE_KEY;

public class OffersRepositoryForTests implements OfferRepository {

    List<Offer> offersList = new ArrayList<>();

    @Override
    public List<Offer> findAll() {
        return offersList;
    }

    @Override
    public Optional<Offer> findByUrl(String url) {
        return offersList.stream()
                .filter(offer -> url.equals(offer.offerUrl()))
                .findFirst();
    }

    @Override
    public Optional<Offer> findById(String id) {
        return offersList.stream()
                .filter(offer -> id.equals(offer.id()))
                .findFirst();
    }

    @Override
    public Offer save(Offer offer) {
        final Optional<Offer> foundOffer = findByUrl(offer.offerUrl());
        if (!foundOffer.isPresent()) {
            offersList.add(offer);
        } else {
            throw new DuplicateOfferKeyException(DUPLICATE_KEY);
        }
        return offer;
    }
    
    @Override
    public List<Offer> saveAll(List<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public void deleteAll() {
        offersList.clear();
    }

    @Override
    public void deleteByUrl(String url) {
        offersList = offersList.stream()
                .filter(offer -> !url.equals(offer.offerUrl()))
                .toList();
    }

    @Override
    public int size() {
        return offersList.size();
    }

    @Override
    public boolean existsByUrl(String url) {
        long count = offersList.stream()
                .filter(offer -> offer.offerUrl().equals(url))
                .count();
        return count == 1;
    }
}
