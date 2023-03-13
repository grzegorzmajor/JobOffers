package ovh.major.joboffers.domain.loginandregister;

public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found!";

    private final UsersRepository usersRepository;

    public LoginAndRegisterFacade(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(user -> new User(user.id(), user.name(), user.password()))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    public RegistrationResult register(User registeredUser) {
        final User user = User.builder()
                .name(registeredUser.name())
                .password(registeredUser.password())
                .build();
        User savedUser = usersRepository.save(user);
        return new RegistrationResult(savedUser.id(), savedUser.name(), true);
    }
}
