package ovh.major.joboffers.domain.loginandregister;

import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;

public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found!";

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
        User savedUser = usersRepository.save(user);
        return new RegistrationResultDto(savedUser.id(), savedUser.name(), true);
    }
}
