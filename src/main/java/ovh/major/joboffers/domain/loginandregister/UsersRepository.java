package ovh.major.joboffers.domain.loginandregister;

import java.util.Optional;

public interface UsersRepository {

    Optional<User> findByUsername(String username);

    User save(User user);
}
