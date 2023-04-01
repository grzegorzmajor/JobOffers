package ovh.major.joboffers.infrastructure.offer.controler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class RequestRestController {

    @PostMapping("/offers")
    public ResponseEntity<RequestDataDto> offers(@RequestBody RequestDataDto request) {
        if (request.offersFilter() ) {
            log.info("wysłano żądanie ofert z filtrem");
        } else {
            log.info("wysłano żądanie wszystkich ofert");
        };
        RequestDataDto response = RequestDataDto.builder()
                .offersFilter(request.offersFilter())
                .build();
        return ResponseEntity.ok(response);
    }
}
