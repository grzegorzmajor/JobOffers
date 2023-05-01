package ovh.major.joboffers.domain.loginandregister.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserDto(

        @Schema(
                description = "User name",
                example = "userName"
        )
        String name,

        @Schema(
                description = "Password",
                example = "pass"
        )
        String password
) {
}
