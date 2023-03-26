package ovh.major.joboffers.infrastructure.offercontroler.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.infrastructure.offercontroler.client.OfferFetcherClient;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="scheduling.enabled", matchIfMissing = false)
class OfferFetcherSchedulerConfig {



}