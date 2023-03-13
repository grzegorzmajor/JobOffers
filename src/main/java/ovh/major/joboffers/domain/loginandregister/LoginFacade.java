package ovh.major.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginFacade {

    private static final String USER_NOT_FOUND = "User not found!";

    private final UsersRepository usersRepository;

    public User findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(user -> new User(user.id(), user.name(), user.password()))
                .orElseThrow();
    }
}
