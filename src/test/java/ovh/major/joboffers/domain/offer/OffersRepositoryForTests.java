package ovh.major.joboffers.domain.offer;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.DUPLICATED_OFFER;

public class OffersRepositoryForTests implements OfferRepository {

    List<Offer> offersList = new ArrayList<>();

    @Override
    public List<Offer> findAll() {
        return offersList;
    }

    @Override
    public List<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public Optional<Offer> findByOfferUrl(String url) {
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
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public <S extends Offer> S save(S entity) {
        if (offersList.stream().anyMatch(offer -> offer.offerUrl().equals(entity.offerUrl()))) {
            throw new DuplicateOfferException(DUPLICATED_OFFER);
        }
        UUID id = UUID.randomUUID();
        Offer offer = new Offer(
                id.toString(),
                entity.position(),
                entity.company(),
                entity.salary(),
                entity.offerUrl()
        );
        offersList.add(offer);
        return (S) offer;
    }

    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .toList();
    }

    @Override
    public void deleteAll() {
        offersList.clear();
    }

    @Override
    public void deleteByOfferUrl(String url) {
        offersList = offersList.stream()
                .filter(offer -> !url.equals(offer.offerUrl()))
                .toList();
    }

    public int size() {
        return offersList.size();
    }

    @Override
    public boolean existsByOfferUrl(String url) {
        long count = offersList.stream()
                .filter(offer -> offer.offerUrl().equals(url))
                .count();
        return count == 1;
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }
}
