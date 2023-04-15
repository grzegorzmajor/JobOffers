package ovh.major.joboffers.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ovh.major.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import ovh.major.joboffers.domain.loginandregister.dto.UserDto;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = loginAndRegisterFacade.findByName(username);
        return getUser(userDto);
    }

    private org.springframework.security.core.userdetails.User getUser(UserDto user) {
        return new org.springframework.security.core.userdetails.User(
                user.name(),
                user.password(),
                Collections.emptyList());
    }
}
