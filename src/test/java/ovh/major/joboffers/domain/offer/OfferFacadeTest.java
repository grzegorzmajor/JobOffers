package ovh.major.joboffers.domain.offer;

import org.junit.jupiter.api.Test;
import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferKeyException;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.DUPLICATE_KEY;
import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.OFFER_NOT_FOUND;

public class OfferFacadeTest {

    private final OffersRepositoryForTests offerRepository = new OffersRepositoryForTests();

    private final OfferFacade offerFacade = new OfferFacade(offerRepository);

    @Test
    public void shouldSave4OffersWhenThereAreNoOffersInDatabase() {
        //when
        offerFacade.saveOffer(new OfferDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDto("tittle4", "company4", "sallary4", "url4"));

        //then
        assertThat(offerRepository.size(),is(equalTo(4)));

    }

    @Test
    public void shouldBeDeletedAllWhenDeleteAllOffers() {
        //given
        offerFacade.saveOffer(new OfferDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDto("tittle4", "company4", "sallary4", "url4"));

        //when
        offerFacade.deleteAllOffers();

        //then
        assertThat(offerRepository.size(),is(equalTo(0)));

    }

    @Test
    public void shouldBeDeletedWhenDeleteOfferByUrl() {
        //given
        offerFacade.saveOffer(new OfferDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDto("tittle4", "company4", "sallary4", "url4"));

        //when
        offerFacade.deleteOfferByUrl("url4");

        //then
        assertAll(
                () -> assertThat(offerRepository.size(),is(equalTo(3))),
                () -> assertThrows(OfferNotFoundException.class, () -> offerFacade.findOfferByUrl("url4"))
        );

    }

    @Test
    public void shouldThrowDuplicateKeyExceptionWhenWithOfferUrlExist() {
        //given
        offerFacade.saveOffer(new OfferDto("tittle1", "company1", "sallary1", "url1"));

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(new OfferDto("tittle2", "company2", "sallary2", "url1")));

        //then
        assertAll(
                () -> assertThat(thrown,is(instanceOf(DuplicateOfferKeyException.class))),
                () -> assertThat(offerRepository.size(),is(equalTo(1))),
                () -> assertThat(thrown.getMessage(),is(equalTo( new DuplicateOfferKeyException(DUPLICATE_KEY).getMessage())))
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenOfferNotFound() {
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferByUrl("url"));

        //then
        assertAll(
                () -> assertThat(thrown,is(instanceOf(OfferNotFoundException.class))),
                () -> assertThat(thrown.getMessage(),is(equalTo( new OfferNotFoundException(OFFER_NOT_FOUND).getMessage())))

        );
    }

    @Test
    public void shouldFetchFromJobsFromRemoteAndSaveOffersWhenRepositoryIsEmpty() {

    }

    @Test
    public void shouldFindOfferByUrlWhenOfferWasSaved() {
        //given
        OfferDto offerDto = new OfferDto("tittle1", "company1", "sallary1", "url1");
        offerFacade.saveOffer(new OfferDto("tittle1", "company1", "sallary1", "url1"));

        //when
        OfferDto result = offerFacade.findOfferByUrl(offerDto.offerUrl());

        //then
        assertAll(
                () -> assertThat(result.offerUrl(),is(equalTo(offerDto.offerUrl()))),
                () -> assertEquals(result,offerDto)
        );

    }

}
