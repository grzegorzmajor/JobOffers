package ovh.major.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    public Optional<List<Offer>> findAllOffers();

    public Optional<Offer> findOfferById(String id);

    public Offer save(Offer offer);

    public boolean fetchAllOffersAndSaveAllIfNoExists();

    public void deleteAll();

    public void deleteOfferById(String id);

}
