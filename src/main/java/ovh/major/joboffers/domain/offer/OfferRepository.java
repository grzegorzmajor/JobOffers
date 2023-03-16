package ovh.major.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    List<Offer> findAll();

    Optional<Offer> findByUrl(String url);

    Optional<Offer> findById(String id);

    Offer save(Offer offer);

    List<Offer> saveAll(List<Offer> offers);

    void deleteAll();

    void deleteByUrl(String url);

    int size();

    boolean existsByUrl(String url);

}
