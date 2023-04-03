package ovh.major.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ovh.major.joboffers.BaseIntegrationTest;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioUserWantToSeeJobOffersIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OfferFetcherScheduler offerFetcherScheduler;

    @Test
    public void shouldOfferFetcherReturnZeroOffer() throws Exception {

        //#klient chce pobrać dostępne oferty ale musi być zalogowany
        //1.nie ma ofert na serwerze
        //2.apka odpytuje zewnętrzną bazę i dodaje 0 ofert
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type","application/json")
                        .withBody("[]")
                )
        );
        //when
        List<OfferDBResponseDto> jobResponse = offerFetcherScheduler.schedule();

        //then
        assertThat(jobResponse.size(),is(equalTo(0)));

        //3.użytkownik próbuje się zalogować i otrzymuje brak autoryzacji 401
        //4.użytkownik próbuje pobrać oferty i otrzymuje brak autoryzacji 401
        //5.użytkownik nie posiada konta i chce się zarejestrować
        //6.użytkownik wypełnia formularz rejestracji i go wysyła status 200
        //7.użytkownik próbuje się zalogować , jeśli logowanie jest poprawne otrzymuje token status 200
        //8.użytkownik próbuje pobrać oferty z poprawnym tokenem w bazie nie ma ofert  otrzumuje o ofert status 200
        //9.w zewnętrznej bazie są nowe oferty
        //10.apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //11.Użytkownik próbuje pobrać nieistniejącą ofertę – otrzymuje 404
        //12. Użytkownik probuje pobrać istniejącą ofertę – otrzymuje ją z kodem 200
        //13.apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //14.jeśli nie ma ofert lub od ostatniego zapytania upłynęło 3 godziny to zostaje odpytana zdalna baza


        //15.użytkownik wysyła zapytanie o oferty otrzymuje oferty z kodem 200
        //given
        //when
        ResultActions performGet = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResultGet = performGet.andExpect(status().isOk()).andReturn();
        String responseGet = mvcResultGet.getResponse().getContentAsString();
        List<OfferDBResponseDto> offersGet = objectMapper.readValue(responseGet, new TypeReference<>() {
        });
        assertAll(
                () -> assertThat(offersGet,is(empty())),
                () -> assertThat(offersGet.size(), is(not(4)))
        );

        //16. dodawanie oferty
        //given
        String contentOfferJson= """
                        {
                        "position" : "junior",
                        "company": "firma krzak",
                        "salary": "free",
                        "offerUrl": "poszukaj se sam"
                        }
                        """.trim();
        //when
        ResultActions performPost = mockMvc.perform(post("/offers")
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResultPost = performPost.andExpect(status().isOk()).andReturn();
        String responsePost = mvcResultPost.getResponse().getContentAsString();

        assertAll(
                () -> assertNotNull(responsePost)
        );


        //17.wylogowanie ręczne lub auto.
    }
}
