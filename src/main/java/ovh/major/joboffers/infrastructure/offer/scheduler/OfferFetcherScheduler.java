package ovh.major.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;

    @Scheduled(fixedDelayString = "${offer.scheduler.delay}")
    public List<OfferDBResponseDto> schedule() {
        log.info("------------");
        log.info("Scheduler: Fetching offer from external server.");
        final List<OfferDBResponseDto> addedOffer = offerFacade.fetchAllOffersAndSaveAllIfNotExists();
        log.info("Added new {} offers ", addedOffer.size());
        log.info("Scheduler: Fetching ended.");
        return addedOffer;
    }

}
