package ovh.major.joboffers.infrastructure.loginandregister.controler.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record TokenRequestDto (

        @Schema(
                description = "User name",
                example = "userName",
                required = true
        )
        @NotBlank(message = "{username.not.blank}")
        String name,

        @Schema(
                description = "Password",
                example = "pass",
                required = true
        )
        @NotBlank(message = "{password.not.blank}")
        String password
){
}
