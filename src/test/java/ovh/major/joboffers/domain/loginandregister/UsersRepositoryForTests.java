package ovh.major.joboffers.domain.loginandregister;

import ovh.major.joboffers.domain.loginandregister.exceptions.UserIsExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.USER_EXISTS;

public class UsersRepositoryForTests implements UsersRepository {
    List<User> usersList = new ArrayList<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return usersList.stream()
                .filter(user -> username.equals(user.name()))
                .findFirst();
    }

    @Override
    public User save(User user) {
        final Optional<User> foundUser = findByUsername(user.name());
        if (!foundUser.isPresent()) {
            usersList.add(user);
        } else {
            throw new UserIsExistException(USER_EXISTS);
        }
        return user;
    }

    public int size() {
        return usersList.size();
    }
}
