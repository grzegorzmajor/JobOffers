package ovh.major.joboffers.domain.loginandregister;

import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;
import ovh.major.joboffers.domain.loginandregister.exceptions.UserIsExistException;
import ovh.major.joboffers.domain.loginandregister.exceptions.UserNotFoundException;

import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.USER_NOT_FOUND;

public class LoginAndRegisterFacade {

    private final UsersRepository usersRepository;

    public LoginAndRegisterFacade(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserDto findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.name(), user.password()))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = User.builder()
                .name(registerUserDto.name())
                .password(registerUserDto.password())
                .build();
        User savedUser;
        try {
            savedUser = usersRepository.save(user);
        } catch (UserIsExistException exception) {
            return new RegistrationResultDto(null, registerUserDto.name(), false);
        }
        return new RegistrationResultDto(savedUser.id(), savedUser.name(), true);
    }
}
