package ovh.major.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    List<Offer> findAllOffers();

    Optional<Offer> findOfferByUrl(String url);

    Offer save(Offer offer);

    boolean fetchAllOffersAndSaveAllIfNoExists();

    void deleteAll();

    void deleteOfferByUrl(String url);

    int size();

}
