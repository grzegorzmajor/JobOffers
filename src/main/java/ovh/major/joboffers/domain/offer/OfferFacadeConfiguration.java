package ovh.major.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
class OfferFacadeConfiguration {

    @Bean
    OfferFacade offerFacade(OfferFetchable offerFetchable) {
        OfferRepository repo = new OfferRepository() {
            @Override
            public boolean existsByUrl(String offerUrl) {
                return false;
            }

            @Override
            public Optional<Offer> findByUrl(String offerUrl) {
                return Optional.empty();
            }

            @Override
            public List<Offer> saveAll(List<Offer> offers) {
                return null;
            }

            @Override
            public void deleteAll() {

            }

            @Override
            public void deleteByUrl(String url) {

            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public List<Offer> findAll() {
                return null;
            }

            @Override
            public Optional<Offer> findById(String id) {
                return Optional.empty();
            }

            @Override
            public Offer save(Offer offer) {
                return null;
            }
        };
        OfferService offerService = new OfferService(offerFetchable, repo);
        return new OfferFacade(repo, offerService);
    }
}
