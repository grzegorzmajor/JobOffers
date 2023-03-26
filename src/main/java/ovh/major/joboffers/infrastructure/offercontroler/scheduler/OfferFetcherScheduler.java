package ovh.major.joboffers.infrastructure.offercontroler.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.domain.offer.dto.OfferDto;

import java.util.List;


@Component
@AllArgsConstructor
@Log4j2
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;

    @Scheduled(cron = "0 0 8 * * 2,5")
    //@Scheduled(cron = "*/5 * * * * *")
    public List<OfferDto> schedule() {
        log.info("------------");
        log.info("Scheduler: Fetching offer from external server.");
        final List<OfferDto> addedOffer = offerFacade.fetchAllOffersAndSaveAllIfNotExists();
        log.info("Added new {} offers ", addedOffer.size());
        log.info("Scheduler: Fetching ended.");
        return addedOffer;
    }

}
