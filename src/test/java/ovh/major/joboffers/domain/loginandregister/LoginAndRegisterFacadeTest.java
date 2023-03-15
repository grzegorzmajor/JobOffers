package ovh.major.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;
import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginAndRegisterFacade;

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        //given
        String username = "user";

        //when
       // Throwable thrown = catchTrowable()

        //then

    }

    @Test
    public void shouldFindUserByUserName() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("name", "pass");
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        //when
        UserDto userResult = loginAndRegisterFacade.findByUsername(register.name());

        //then
        assertThat(userResult,is(equalTo(new UserDto(register.id(), "name", "pass"))));

    }

    @Test
    public void shouldRegister_user() {

    }
}
