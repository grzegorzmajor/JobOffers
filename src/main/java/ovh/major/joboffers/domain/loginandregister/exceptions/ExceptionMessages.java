package ovh.major.joboffers.domain.loginandregister.exceptions;

public enum ExceptionMessages {
    USER_EXISTS("A user with that name exists!"),
    USER_NOT_FOUND("User not found!"),
    BAD_CREDENTIALS("Bad Credentials");

    private String s;

    ExceptionMessages(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

}
