package ovh.major.joboffers.infrastructure.loginandregister.controler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record JwtResponseDto (

        @Schema(
                description = "Server-generated token"
        )
        String token,


        @Schema(
                description = "User name"
        )
        String name
){
}
