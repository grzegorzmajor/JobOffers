package ovh.major.joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ovh.major.joboffers.BaseIntegrationTest;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;
import ovh.major.joboffers.infrastructure.offercontroler.client.OfferFetcherClient;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class TypicalScenarioUserWantToSeeJobOffersIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OfferFetcherClient offerFetcherClient;

    @Test
    public void shouldOfferFetcherReturnZeroOffer() {

        //#klient chce pobrać dostępne oferty ale musi być zalogowany
        //1.nie ma ofert na serwerze
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type","application/json")
                        .withBody("[]")
                )
        );
        //when
        List<OfferExternalResponseDto> jobResponse = offerFetcherClient.fetchOffers();

        //then
        assertThat(jobResponse.size(),is(equalTo(0)));

        //2.apka odpytuje zewnętrzną bazę i dodaje 0 ofert
        //3.użytkownik próbuje się zalogować i otrzymuje brak autoryzacji 401
        //4.użytkownik próbuje pobrać oferty i otrzymuje brak autoryzacji 401
        //5.użytkownik nie posiada konta i chce się zarejestrować
        //6.użytkownik wypełnia formularz rejestracji i go wysyła status 200
        //7.użytkownik próbuje się zalogować , jeśli logowanie jest poprawne otrzymuje token statur 200
        //8.użytkownik próbuje pobrać oferty z poprawnym tokenem w bazie nie ma ofert  otrzumuje o ofert status 200
        //9.w zewnętrznej bazie są nowe oferty
        //10. apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //11.Użytkownik próbuje pobrać nieistniejącą ofertę – otrzymuje 404
        //12. Użytkownik probuje pobrać istniejącą ofertę – otrzymuje ją z kodem 200
        //13.apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //14.jeśli nie ma ofert lub od ostatniego zapytania upłynęło 3 godziny to zostaje odpytana zdalna baza
        //15.użytkownik wysyła zapytanie o oferty otrzymuje oferty z kodem 200
        //16.wylogowanie ręczne lub auto.
    }
}
