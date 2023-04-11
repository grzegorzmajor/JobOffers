package ovh.major.joboffers.infrastructure.offer.controler;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;

import java.util.List;

@RestController
@RequestMapping("/offers")
@Log4j2
@AllArgsConstructor
public class OfferController {

    private final OfferFacade offerFacade;

    @PostMapping
    public ResponseEntity<OfferDBResponseDto> addOffers(@RequestBody @Valid OfferDBRequestDto offerToAdd) {
        OfferDBResponseDto offerDBResponseDto = offerFacade.saveOffer(offerToAdd);
        return ResponseEntity.ok(offerDBResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<OfferDBResponseDto>> findAllOffers() {
        List<OfferDBResponseDto> allOffer = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDBResponseDto> findOfferById(@PathVariable String id) {
        OfferDBResponseDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }
}
