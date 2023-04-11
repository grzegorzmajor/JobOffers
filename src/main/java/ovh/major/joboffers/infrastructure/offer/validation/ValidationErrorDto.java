package ovh.major.joboffers.infrastructure.offer.validation;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ValidationErrorDto(
        List<String> messages,
        HttpStatus status
) {
}
