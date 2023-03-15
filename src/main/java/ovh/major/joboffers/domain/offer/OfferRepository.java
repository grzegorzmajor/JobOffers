package ovh.major.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    Optional<List<Offer>> findAllOffers();

    Optional<Offer> findOfferById(String id);

    Offer save(Offer offer);

    boolean fetchAllOffersAndSaveAllIfNoExists();

    void deleteAll();

    void deleteOfferById(String id);

    int size();

}
