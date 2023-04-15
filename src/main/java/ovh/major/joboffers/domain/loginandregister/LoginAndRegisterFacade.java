package ovh.major.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;
import ovh.major.joboffers.domain.loginandregister.exceptions.UserIsExistException;

import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.USER_NOT_FOUND;

@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UsersRepository repository;

    public UserDto findByName(String name) {
        return repository.findByName(name)
                .map(user -> new UserDto(user.id(), user.name(), user.password()))
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND.toString()) );
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = User.builder()
                .name(registerUserDto.name())
                .password(registerUserDto.password())
                .build();
        User savedUser = repository.save(user);
        return new RegistrationResultDto(savedUser.id(), savedUser.name(), true);
    }
}
