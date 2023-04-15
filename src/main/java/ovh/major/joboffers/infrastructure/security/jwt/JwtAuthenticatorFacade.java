package ovh.major.joboffers.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.JwtResponseDto;
import ovh.major.joboffers.infrastructure.loginandregister.controler.dto.TokenRequestDto;

import java.time.*;
import com.auth0.jwt.algorithms.Algorithm;

@AllArgsConstructor
@Component
public class JwtAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;

    public JwtResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequestDto.name(), tokenRequestDto.password()));
        User user = (User) authenticate.getPrincipal();
        String token = createToken(user);
        String name = user.getUsername();
        return JwtResponseDto.builder()
                .token(token)
                .name(name)
                .build();
    }

    private String createToken(org.springframework.security.core.userdetails.User user) {
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = now.plus(Duration.ofDays(properties.expirationDays()));
        String issuer = properties.issuer();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }

}
