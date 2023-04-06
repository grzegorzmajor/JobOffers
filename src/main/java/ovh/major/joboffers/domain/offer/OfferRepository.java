package ovh.major.joboffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    Optional<Offer> findByOfferUrl(String url);

    void deleteByOfferUrl(String url);

    boolean existsByOfferUrl(String url);

}
