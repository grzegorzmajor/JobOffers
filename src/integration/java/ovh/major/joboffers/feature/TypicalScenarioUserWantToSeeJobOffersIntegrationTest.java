package ovh.major.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ovh.major.joboffers.BaseIntegrationTest;
import ovh.major.joboffers.SampleOfferResponse;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.exceptions.ExceptionMessages;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.JwtResponseDto;
import ovh.major.joboffers.infrastructure.offer.controler.error.OfferControllerErrorResponse;
import ovh.major.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
        List<OfferDBResponseDto> jobResponse2 = offerFetcherScheduler.schedule();

        //then
        assertThat(jobResponse2.size(), is(equalTo(0)));

        //3.użytkownik próbuje się zalogować i otrzymuje brak autoryzacji 401
        //given
        //when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "name": "user",
                        "password": "pass"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                            {
                            "message": "Bad Credentials",
                            "status": "UNAUTHORIZED"
                            }
                        """.trim()));


        //4.użytkownik próbuje pobrać oferty i otrzymuje brak autoryzacji 401 /brak tokena
        //given
        //when
        ResultActions failedGetOffersRequest4 = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        failedGetOffersRequest4.andExpect(status().isForbidden());

        //5.użytkownik nie posiada konta i chce się zarejestrować, wysyła formularz rejest. i otrzymuje status 201
        //given
        //when
        ResultActions registerAction5 = mockMvc.perform(post("/register")
                .content("""
                        {
                        "name": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        MvcResult registerActionResult5 = registerAction5.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson5 = registerActionResult5.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto5 = objectMapper.readValue(registerActionResultJson5, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto5.name(),is(equalTo("someUser"))),
                () -> assertTrue(registrationResultDto5.registered()),
                () -> assertThat(registrationResultDto5.id(),is(not(nullValue())))
        );

        //6.użytkownik próbuje się zalogować , jeśli logowanie jest poprawne otrzymuje token status 200

        ResultActions loginRequest6 = mockMvc.perform(post("/token")
                .content("""
                        {
                        "name": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        MvcResult mvcResultLoginRequest6 = loginRequest6.andExpect(status().isOk()).andReturn();

        String responseJson6 = mvcResultLoginRequest6.getResponse().getContentAsString();
        JwtResponseDto response6 = objectMapper.readValue(responseJson6, new TypeReference<>() {
        });
        assertAll(
                () -> assertTrue(response6.name().contains("someUser")),
                () -> assertTrue(!response6.token().isEmpty()),
                () -> assertTrue(!response6.token().isBlank()),
                () -> Assertions.assertThat(response6.token()).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );



        //7.użytkownik próbuje pobrać oferty z poprawnym tokenem w bazie nie ma ofert  otrzumuje 0 ofert status 200
        //given
        String token = response6.token();
        //when
        ResultActions performGet = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
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

        //8.w zewnętrznej bazie są 2 nowe oferty
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

        //9.apka odpytuje zewnętrzny serwer i dodaje nowe oferty
        //given
        //when
        List<OfferDBResponseDto> jobResponse9 = offerFetcherScheduler.schedule();

        //then
        assertThat(jobResponse9.size(), is(equalTo(2)));


        //10.Użytkownik próbuje pobrać nieistniejącą ofertę – otrzymuje 404 - not found
        //given
        //when
        ResultActions performGetWithNotExistingId = mockMvc.perform(get("/offers/notexistingid")
                .header("Authorization", "Bearer " + token)
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

        //11.Użytkownik probuje pobrać istniejącą ofertę – otrzymuje ją z kodem 200
        //given
        String contentOfferJson11 = """
                {
                "position" : "junior",
                "company": "firma krzak",
                "salary": "free",
                "offerUrl": "pobierz oferte"
                }
                """.trim();
        ResultActions performPost11 = mockMvc.perform(post("/offers")
                .header("Authorization", "Bearer " + token)
                .content(contentOfferJson11)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultPost11 = performPost11.andExpect(status().isCreated()).andReturn();
        String responsePost11 = mvcResultPost11.getResponse().getContentAsString();
        OfferDBResponseDto offerPostResult11 = objectMapper.readValue(responsePost11, new TypeReference<>() {
        });
        String offerId = offerPostResult11.id();

        ResultActions performGetWithExistingId = mockMvc.perform(get("/offers/" + offerId)
                .header("Authorization", "Bearer " + token)
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

        //12.użytkownik wysyła zapytanie o oferty i otrzymuje oferty z kodem 200
        //given
        //when
        ResultActions performGet12 = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        MvcResult mvcResultGet12 = performGet12.andExpect(status().isOk()).andReturn();
        String responseGet12 = mvcResultGet12.getResponse().getContentAsString();
        List<OfferDBResponseDto> response12 = objectMapper.readValue(responseGet12, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(response12.size(), is(not(0))),
                () -> assertThat(mvcResultGet12.getResponse().getStatus(), is(equalTo(200)))
        );

        //13. użytkownik dodaje ofertę i otrzymuję ją w odpowiedzi z nadanym numerem id oraz otrzymuje status 201
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
        ResultActions performPost13 = mockMvc.perform(post("/offers")
                .header("Authorization", "Bearer " + token)
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResultPost13 = performPost13.andExpect(status().isCreated()).andReturn();
        String responsePost13 = mvcResultPost13.getResponse().getContentAsString();
        OfferDBResponseDto offerPostResult13 = objectMapper.readValue(responsePost13, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(offerPostResult13.salary(), is(equalTo("free"))),
                () -> assertThat(offerPostResult13.offerUrl(), is(equalTo("poszukaj se sam"))),
                () -> assertThat(offerPostResult13.company(), is(equalTo("firma krzak"))),
                () -> assertThat(offerPostResult13.position(), is(equalTo("junior"))),
                () -> assertNotNull(offerPostResult13.id()),
                () -> assertThat(mvcResultPost13.getResponse().getStatus(), is(equalTo(201)))
        );

        //14.uzytkownik usuwa dodaną ofertę i otrzymuje status 204 no content oraz oferta zostaje usunięta.
        //given
        String offerIdToDelete = offerPostResult13.id(); //offerta z poprzedniego kroku 13.

        //when
        ResultActions performPost14 = mockMvc.perform(post("/offers/" + offerIdToDelete)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        performPost14.andExpect(status().isNoContent());
        MvcResult mvcResultPost14 = performPost14.andExpect(status().isNoContent()).andReturn();

        ResultActions performGet14 = mockMvc.perform(get("/offers/" + offerIdToDelete)
                .header("Authorization", "Bearer " + token)
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultGet14 = performGet14.andExpect(status().isNotFound()).andReturn();
        String responseGet14 = mvcResultGet14.getResponse().getContentAsString();
        OfferControllerErrorResponse errorResponse14 = objectMapper.readValue(responseGet14, new TypeReference<>() {
        });

        assertAll(
                () -> assertThat(mvcResultPost14.getResponse().getStatus(), is(equalTo(204))),
                () -> assertThat(mvcResultGet14.getResponse().getStatus(), is(equalTo(404))),
                () -> assertThat(errorResponse14.message(), is(containsString(ExceptionMessages.OFFER_NOT_FOUND.toString()))),
                () -> assertThat(errorResponse14.status(), is(equalTo(HttpStatus.NOT_FOUND)))
        );

        //15.wylogowanie ręczne lub auto.
    }
}
