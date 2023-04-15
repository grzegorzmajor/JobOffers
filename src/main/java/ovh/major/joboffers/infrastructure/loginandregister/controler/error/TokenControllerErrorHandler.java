package ovh.major.joboffers.infrastructure.loginandregister.controler.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.BAD_CREDENTIALS;


@ControllerAdvice
public class TokenControllerErrorHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public TokenErrorResponse handleBadCredentials() {
        return new TokenErrorResponse(BAD_CREDENTIALS.toString(), HttpStatus.UNAUTHORIZED);
    }
}
