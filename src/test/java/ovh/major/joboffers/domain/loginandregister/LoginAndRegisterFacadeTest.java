package ovh.major.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.USER_NOT_FOUND;


public class LoginAndRegisterFacadeTest {

    final RegisterUserDto registerUserDto = new RegisterUserDto("name", "pass");
    private final UsersRepositoryForTests usersRepository = new UsersRepositoryForTests();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(usersRepository);

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        //given
        final String username = "user";

        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByName(username));

        //then
        assertAll(
                () -> assertThat(thrown, is(instanceOf(BadCredentialsException.class))),
                () -> assertThat(thrown.getMessage(), is(equalTo(USER_NOT_FOUND.toString())))
        );
    }

    @Test
    public void shouldFindUserByUserName() {
        //given
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //when
        UserDto userResult = loginAndRegisterFacade.findByName(register.name());

        //then
        assertThat(userResult, is(equalTo(new UserDto(register.id(), "name", "pass"))));

    }

    @Test
    public void shouldRegisterUserIfNotExist() {
        //when
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertAll(
                () -> assertThat(register.registered(), is(true)),
                () -> assertThat(loginAndRegisterFacade.findByName(register.name()).name(),
                        is(equalTo(registerUserDto.name())))
        );
    }
}
