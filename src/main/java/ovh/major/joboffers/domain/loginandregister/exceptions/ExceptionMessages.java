package ovh.major.joboffers.domain.loginandregister.exceptions;

public enum ExceptionMessages {
    USER_EXISTS("A user with that name exists!"),
    USER_NOT_FOUND("User not found!");

    ExceptionMessages(String s) {
    }
}
