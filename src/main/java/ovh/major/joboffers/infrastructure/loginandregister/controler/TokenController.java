package ovh.major.joboffers.infrastructure.loginandregister.controler;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.JwtResponseDto;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.TokenRequestDto;
import ovh.major.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;

@RestController
@Log4j2
@AllArgsConstructor
public class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    @Operation(summary = "User login and token assignment.",
            description = "Paste the token into the field after clicking the authorization button above for all functionalities to work.")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtResponse = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.ok(jwtResponse);
    }

}