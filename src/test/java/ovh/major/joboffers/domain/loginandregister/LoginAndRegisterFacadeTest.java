package ovh.major.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;
import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;
import ovh.major.joboffers.domain.loginandregister.exceptions.UserNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ovh.major.joboffers.domain.loginandregister.exceptions.ExceptionMessages.USER_NOT_FOUND;


public class LoginAndRegisterFacadeTest {

    private final UsersRepositoryForTests usersRepository = new UsersRepositoryForTests();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(usersRepository);

    final RegisterUserDto registerUserDto = new RegisterUserDto("name", "pass");

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        //given
        final String username = "user";

        //when
       Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByUsername(username));

        //then
        assertAll(
                () -> assertThat(thrown,is(instanceOf(UserNotFoundException.class))),
                () -> assertThat(thrown.getMessage(),is(equalTo(new UserNotFoundException(USER_NOT_FOUND).getMessage())))
        );
    }

    @Test
    public void shouldFindUserByUserName() {
        //given
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //when
        UserDto userResult = loginAndRegisterFacade.findByUsername(register.name());

        //then
        assertThat(userResult,is(equalTo(new UserDto(register.id(), "name", "pass"))));

    }

    @Test
    public void shouldRegisterUserIfNotExist() {
        //when
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertAll(
                () -> assertThat(register.registered(), is(true )),
                () -> assertThat(loginAndRegisterFacade.findByUsername(register.name()).name(),
                        is(equalTo(registerUserDto.name())))
        );
    }

    @Test
    public void shouldNotRegisterUserAndThrowExceptionIfUserExists() {
        //given
        loginAndRegisterFacade.register(registerUserDto);

        //when
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertAll(
                () -> assertFalse(register.registered()),
                () -> assertThat(register.name(),is(equalTo(registerUserDto.name()))),
                () -> assertThat(register.id(),is(nullValue())),
                () -> assertThat(usersRepository.size(), is(equalTo(1)))
        );

    }
}
