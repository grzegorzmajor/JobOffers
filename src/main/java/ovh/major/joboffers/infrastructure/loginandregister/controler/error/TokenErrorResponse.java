package ovh.major.joboffers.infrastructure.loginandregister.controler.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponse(String message, HttpStatus status) {
}
