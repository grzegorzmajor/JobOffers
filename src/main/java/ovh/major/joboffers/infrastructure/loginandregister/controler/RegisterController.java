package ovh.major.joboffers.infrastructure.loginandregister.controler;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovh.major.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import ovh.major.joboffers.domain.loginandregister.dto.RegisterUserDto;
import ovh.major.joboffers.domain.loginandregister.dto.RegistrationResultDto;

@RestController
@Log4j2
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    @Operation(summary = "User registration.")
    public ResponseEntity<RegistrationResultDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registrationResult = loginAndRegisterFacade.register(
          new RegisterUserDto(registerUserDto.name(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResult);
    }
}
