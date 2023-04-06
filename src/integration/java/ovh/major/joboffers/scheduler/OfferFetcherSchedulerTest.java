package ovh.major.joboffers.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ovh.major.joboffers.BaseIntegrationTest;
import ovh.major.joboffers.JobOffersSpringBootApplication;
import ovh.major.joboffers.domain.offer.OfferFetchable;

import java.time.Duration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = JobOffersSpringBootApplication.class, properties = "scheduling.enabled=true")
public class OfferFetcherSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetchable remoteOfferClient;

    @Test
    public void shouldRunClientOffersFetchingExactlyGivenTimes() {
        await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(remoteOfferClient, times(1)).fetchOffers());
    }


}
