package ovh.major.joboffers.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.GenericContainer;
import ovh.major.joboffers.BaseIntegrationTest;

import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.JwtResponseDto;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class RedisOffersCacheIntegrationTest extends BaseIntegrationTest {

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S" );
    }

    @Test
    public void shouldSaveOfferToCacheAndThenInvalidateByTimeToLive() throws Exception {

        //1.user registering
        //given
        //when
        ResultActions registerAction1 = mockMvc.perform(post("/register")
                .content("""
                        {
                        "name": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        registerAction1.andExpect(status().isCreated());

        //2.login
        //given
        //when

        ResultActions loginRequest2 = mockMvc.perform(post("/token")
                .content("""
                        {
                        "name": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        MvcResult mvcResultLoginRequest2 = loginRequest2.andExpect(status().isOk()).andReturn();
        String responseJson2 = mvcResultLoginRequest2.getResponse().getContentAsString();
        JwtResponseDto response2 = objectMapper.readValue(responseJson2, new TypeReference<>() {});
        String jwtToken = response2.token();

        //3. should save to cache offers request
        //given
        //when
        mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        verify(offerFacade, times(1)).findAllOffers();
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();


        //4: cache should be invalidated
        //given
        //when
        //then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                            mockMvc.perform(get("/offers")
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                            );
                            verify(offerFacade, atLeast(2)).findAllOffers();
                        }
                );

    }
}
