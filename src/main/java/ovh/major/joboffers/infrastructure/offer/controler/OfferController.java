package ovh.major.joboffers.infrastructure.offer.controler;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.major.joboffers.domain.offer.OfferFacade;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/offers")
@Log4j2
@AllArgsConstructor
public class OfferController {

    private final OfferFacade offerFacade;

    @Operation(summary = "Adding new job offer.",
            description = "Requires entering a token - button at the top of the page.")
    @PostMapping
    public ResponseEntity<OfferDBResponseDto> addOffer(@RequestBody @Valid OfferDBRequestDto offerToAdd) {
        OfferDBResponseDto offerDBResponseDto = offerFacade.saveOffer(offerToAdd);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerDBResponseDto);
    }

    @Operation(summary = "Deleting existing job offer.",
            description = "Requires entering a token - button at the top of the page.")
    @PostMapping("/{id}")
    public ResponseEntity deleteOffer(@RequestBody @PathVariable String id) {
        try {
            offerFacade.findOfferById(id);
        } catch (OfferNotFoundException exception) {
            log.error("in delete post request: " + exception.getMessage());
            return ResponseEntity.notFound().build();
        }
        offerFacade.deleteOfferById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns all job offers.",
            description = "Requires entering a token - button at the top of the page.")
    @GetMapping
    public ResponseEntity<List<OfferDBResponseDto>> findAllOffers() {
        List<OfferDBResponseDto> allOffer = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffer);
    }

    @Operation(summary = "Returns a job offer with the specified id.",
            description = "Requires entering a token - button at the top of the page.")
    @GetMapping("/{id}")
    public ResponseEntity<OfferDBResponseDto> findOfferById(@PathVariable String id) {
        OfferDBResponseDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }
}
