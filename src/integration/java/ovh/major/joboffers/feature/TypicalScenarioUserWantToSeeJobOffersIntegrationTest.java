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
import ovh.major.joboffers.SampleOfferResponse;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages;
import ovh.major.joboffers.infrastructure.offer.controler.error.OfferControllerErrorResponse;
import ovh.major.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioUserWantToSeeJobOffersIntegrationTest extends BaseIntegrationTest implements SampleOfferResponse {

    @Autowired
    OfferFetcherScheduler offerFetcherScheduler;

    @Test
    public void user_want_to_see_offers_but_have_to_be_logged_in_and_external_server_should_have_some_offers() throws Exception {

        //#klient chce pobrać dostępne oferty ale musi być zalogowany
        //1.nie ma ofert na serwerze
        //given
        //when
        //then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(zeroOffersJson())
                )
        );

        //2.apka odpytuje zewnętrzną bazę i dodaje 0 ofert
        //given
        //when
        List<OfferDBResponseDto> jobResponse = offerFetcherScheduler.schedule();

        //then
        assertThat(jobResponse.size(), is(equalTo(0)));

        //3.użytkownik próbuje się zalogować i otrzymuje brak autoryzacji 401
        //4.użytkownik próbuje pobrać oferty i otrzymuje brak autoryzacji 401
        //5.użytkownik nie posiada konta i chce się zarejestrować
        //6.użytkownik wypełnia formularz rejestracji i go wysyła status 200
        //7.użytkownik próbuje się zalogować , jeśli logowanie jest poprawne otrzymuje token status 200

        //8.użytkownik próbuje pobrać oferty z poprawnym tokenem w bazie nie ma ofert  otrzumuje 0 ofert status 200
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
                () -> assertThat(responseGet, is(not(equalTo("")))),
                () -> assertThat(offersGet, is(empty())),
                () -> assertThat(offersGet.size(), is(not(4))),
                () -> assertThat(mvcResultGet.getResponse().getStatus(), is(equalTo(200)))
        );

        //9.w zewnętrznej bazie są 2 nowe oferty
        //given
        //when
        //then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(twoOffersJson())
                )
        );

        //10.apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //given
        //when
        List<OfferDBResponseDto> jobResponse10 = offerFetcherScheduler.schedule();

        //then
        assertThat(jobResponse10.size(), is(equalTo(2)));


        //11.Użytkownik próbuje pobrać nieistniejącą ofertę – otrzymuje 404 - not found
        //given
        //when
        ResultActions performGetWithNotExistingId = mockMvc.perform(get("/offers/notexistingid")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        MvcResult mvcResultGetWithNotExistingId = performGetWithNotExistingId.andExpect(status().isNotFound()).andReturn();
        String responseGetWithNotExistingId = mvcResultGetWithNotExistingId.getResponse().getContentAsString();
        OfferControllerErrorResponse errorResponse = objectMapper.readValue(responseGetWithNotExistingId, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(errorResponse.message(), is(containsString(ExceptionMessages.OFFER_NOT_FOUND.toString()))),
                () -> assertThat(errorResponse.status(), is(equalTo(HttpStatus.NOT_FOUND)))
        );

        //12.Użytkownik probuje pobrać istniejącą ofertę – otrzymuje ją z kodem 200
        //given
        String contentOfferJson12 = """
                {
                "position" : "junior",
                "company": "firma krzak",
                "salary": "free",
                "offerUrl": "pobierz oferte"
                }
                """.trim();
        ResultActions performPost12 = mockMvc.perform(post("/offers")
                .content(contentOfferJson12)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultPost12 = performPost12.andExpect(status().isCreated()).andReturn();
        String responsePost12 = mvcResultPost12.getResponse().getContentAsString();
        OfferDBResponseDto offerPostResult12 = objectMapper.readValue(responsePost12, new TypeReference<>() {
        });
        String offerId = offerPostResult12.id();

        ResultActions performGetWithExistingId = mockMvc.perform(get("/offers/" + offerId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResultGetWithExistingId = performGetWithExistingId.andExpect(status().isOk()).andReturn();
        String responseGetWithExistingId = mvcResultGetWithExistingId.getResponse().getContentAsString();
        OfferDBResponseDto offerDBResponseDto = objectMapper.readValue(responseGetWithExistingId, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(offerDBResponseDto.offerUrl(), is(equalTo("pobierz oferte"))),
                () -> assertThat(offerDBResponseDto.salary(), is(equalTo("free"))),
                () -> assertThat(offerDBResponseDto.position(), is(equalTo("junior"))),
                () -> assertThat(offerDBResponseDto.company(), is(equalTo("firma krzak"))),
                () -> assertThat(offerDBResponseDto.id(), is(not(emptyString())))
        );

        //13.użytkownik wysyła zapytanie o oferty i otrzymuje oferty z kodem 200
        //given
        //when
        ResultActions performGet13 = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        MvcResult mvcResultGet13 = performGet13.andExpect(status().isOk()).andReturn();
        String responseGet13 = mvcResultGet13.getResponse().getContentAsString();
        List<OfferDBResponseDto> response13 = objectMapper.readValue(responseGet13, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(response13.size(), is(not(0))),
                () -> assertThat(mvcResultGet13.getResponse().getStatus(), is(equalTo(200)))
        );

        //14. użytkownik dodaje ofertę i otrzymuję ją w odpowiedzi z nadanym numerem id oraz otrzymuje status 201
        //given
        String contentOfferJson = """
                {
                "position" : "junior",
                "company": "firma krzak",
                "salary": "free",
                "offerUrl": "poszukaj se sam"
                }
                """.trim();
        //when
        ResultActions performPost14 = mockMvc.perform(post("/offers")
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResultPost14 = performPost14.andExpect(status().isCreated()).andReturn();
        String responsePost14 = mvcResultPost14.getResponse().getContentAsString();
        OfferDBResponseDto offerPostResult14 = objectMapper.readValue(responsePost14, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(offerPostResult14.salary(), is(equalTo("free"))),
                () -> assertThat(offerPostResult14.offerUrl(), is(equalTo("poszukaj se sam"))),
                () -> assertThat(offerPostResult14.company(), is(equalTo("firma krzak"))),
                () -> assertThat(offerPostResult14.position(), is(equalTo("junior"))),
                () -> assertNotNull(offerPostResult14.id()),
                () -> assertThat(mvcResultPost14.getResponse().getStatus(), is(equalTo(201)))
        );

        //15.uzytkownik usuwa dodaną ofertę i otrzymuje status 204 no content oraz oferta zostaje usunięta.
        //given
        String offerIdToDelete = offerPostResult14.id(); //offerta z poprzedniego kroku 14.

        //when
        ResultActions performPost15 = mockMvc.perform(post("/offers/" + offerIdToDelete)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        performPost15.andExpect(status().isNoContent());
        MvcResult mvcResultPost15 = performPost15.andExpect(status().isNotFound()).andReturn();

        ResultActions performGet15 = mockMvc.perform(get("/offers/" + offerIdToDelete)
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultGet15 = performGet15.andExpect(status().isNotFound()).andReturn();
        String responseGet15 = mvcResultGet15.getResponse().getContentAsString();
        OfferControllerErrorResponse errorResponse15 = objectMapper.readValue(responseGet15, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(mvcResultPost15.getResponse().getStatus(), is(equalTo(204))),
                () -> assertThat(mvcResultGet15.getResponse().getStatus(), is(equalTo(404))),
                () -> assertThat(errorResponse15.message(), is(containsString(ExceptionMessages.OFFER_NOT_FOUND.toString()))),
                () -> assertThat(errorResponse15.status(), is(equalTo(HttpStatus.NOT_FOUND)))
        );

        //16.wylogowanie ręczne lub auto.
    }
}
