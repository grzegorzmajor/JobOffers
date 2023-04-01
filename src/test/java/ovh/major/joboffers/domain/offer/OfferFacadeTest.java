package ovh.major.joboffers.domain.offer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.exceptions.DuplicateOfferException;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages.*;

public class OfferFacadeTest {

    private final OffersRepositoryForTests offerRepository = new OffersRepositoryForTests();

    private final OfferFetcherForTests offerFetcherForTests = new OfferFetcherForTests(List.of(
            new OfferExternalResponseDto("tittle1", "company1", "sallary1", "url1"),
            new OfferExternalResponseDto("tittle2", "company2", "sallary2", "url2"),
            new OfferExternalResponseDto("tittle3", "company3", "sallary3", "url3"),
            new OfferExternalResponseDto("tittle4", "company4", "sallary4", "url4")
    ));

    private final OfferFacade offerFacade = new OfferFacade(offerRepository,new OfferService(offerFetcherForTests, offerRepository));

    @Test
    public void shouldFetchFromJobsFromRemoteAndSaveOffersWhenRepositoryIsEmpty() {
        // given
        Assertions.assertThat(offerFacade.findAllOffers()).isEmpty();

        // when
        List<OfferDBResponseDto> result = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        // then
        Assertions.assertThat(result).hasSize(4);

    }
    @Test
    public void shouldSave4OffersWhenThereAreNoOffersInDatabase() {
        //when
        offerFacade.saveOffer(new OfferDBRequestDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle4", "company4", "sallary4", "url4"));

        //then
        assertThat(offerRepository.size(), is(equalTo(4)));

    }

    @Test
    public void shouldBeDeletedAllWhenDeleteAllOffers() {
        //given
        offerFacade.saveOffer(new OfferDBRequestDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle4", "company4", "sallary4", "url4"));

        //when
        offerFacade.deleteAllOffers();

        //then
        assertThat(offerRepository.size(), is(equalTo(0)));

    }

    @Test
    public void shouldBeDeletedWhenDeleteOfferByUrl() {
        //given
        offerFacade.saveOffer(new OfferDBRequestDto("tittle1", "company1", "sallary1", "url1"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle2", "company2", "sallary2", "url2"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle3", "company3", "sallary3", "url3"));
        offerFacade.saveOffer(new OfferDBRequestDto("tittle4", "company4", "sallary4", "url4"));

        //when
        offerFacade.deleteOfferByUrl("url4");

        //then
        assertAll(
                () -> assertThat(offerRepository.size(), is(equalTo(3))),
                () -> assertThrows(OfferNotFoundException.class, () -> offerFacade.findOfferByUrl("url4"))
        );

    }

    @Test
    public void shouldThrowDuplicateKeyExceptionWhenWithOfferUrlExist() {
        //given
        offerFacade.saveOffer(new OfferDBRequestDto("tittle1", "company1", "sallary1", "url1"));

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(new OfferDBRequestDto("tittle2", "company2", "sallary2", "url1")));

        //then
        assertAll(
                () -> assertThat(thrown, is(instanceOf(DuplicateOfferException.class))),
                () -> assertThat(offerRepository.size(), is(equalTo(1))),
                () -> assertThat(thrown.getMessage(), is(equalTo(new DuplicateOfferException(DUPLICATED_OFFER).getMessage())))
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenOfferNotFound() {
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferByUrl("url"));

        //then
        assertAll(
                () -> assertThat(thrown, is(instanceOf(OfferNotFoundException.class))),
                () -> assertThat(thrown.getMessage(), is(equalTo(new OfferNotFoundException(OFFER_NOT_FOUND).getMessage())))

        );
    }

    @Test
    public void shouldFindOfferByUrlWhenOfferWasSaved() {
        //given
        OfferDBResponseDto offerDBResponseDto = new OfferDBResponseDto(null, "tittle1", "company1", "sallary1", "url1");
        offerFacade.saveOffer(new OfferDBRequestDto("tittle1", "company1", "sallary1", "url1"));

        //when
        OfferDBResponseDto result = offerFacade.findOfferByUrl(offerDBResponseDto.offerUrl());
        OfferDBResponseDto expectedOffer = new OfferDBResponseDto(
                result.id(),
                offerDBResponseDto.position(),
                offerDBResponseDto.company(),
                offerDBResponseDto.salary(),
                offerDBResponseDto.offerUrl()
                );

        //then
        assertAll(
                () -> assertThat(result.offerUrl(), is(equalTo(expectedOffer.offerUrl()))),
                () -> assertEquals(result, expectedOffer)
        );

    }

}
