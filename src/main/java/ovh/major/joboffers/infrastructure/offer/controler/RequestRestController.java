package ovh.major.joboffers.infrastructure.offer.controler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.domain.offer.OfferMapper;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;

import java.util.List;

@RestController
@RequestMapping("/offers")
@Log4j2
@AllArgsConstructor
public class RequestRestController {

    private final OfferFacade offerFacade;

    @PostMapping
    public ResponseEntity<String> offers(@RequestBody OfferDBRequestDto offerToAdd) {
        offerFacade.saveOffer(offerToAdd);
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<List<OfferDBResponseDto>> findAllOffers() {
        List<OfferDBResponseDto> allOffer = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffer);
    }

}
