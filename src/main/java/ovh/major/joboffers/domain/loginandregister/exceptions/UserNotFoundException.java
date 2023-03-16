package ovh.major.joboffers.domain.loginandregister.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(ExceptionMessages message) {
        super(message.toString());
    }
}

