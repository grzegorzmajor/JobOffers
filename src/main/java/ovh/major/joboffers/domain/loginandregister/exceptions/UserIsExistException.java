package ovh.major.joboffers.domain.loginandregister.exceptions;

public class UserIsExistException extends RuntimeException {
    public UserIsExistException(ExceptionMessages message) {
        super(message.toString());
    }
}
