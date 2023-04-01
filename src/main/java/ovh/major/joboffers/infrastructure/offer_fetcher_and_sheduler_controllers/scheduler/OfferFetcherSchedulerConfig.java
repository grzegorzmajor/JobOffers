package ovh.major.joboffers.infrastructure.offer_fetcher_and_sheduler_controllers.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="scheduling.enabled", matchIfMissing = false)
class OfferFetcherSchedulerConfig {

}