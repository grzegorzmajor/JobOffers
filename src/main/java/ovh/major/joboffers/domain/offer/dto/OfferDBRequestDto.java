package ovh.major.joboffers.domain.offer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Schema(
        description = "The object passed as an argument to the methods for communication with the database."
)
@Builder
public record OfferDBRequestDto(

        @Schema(
                description = "Position",
                example = "Junior Java Developer"
        )
        @NotNull(message = "{position.null.validation.message}")
        @NotEmpty(message = "{position.empty.validation.message}")
        String position,

        @Schema(
                description = "Company name",
                example = "Junior Java Developer Development Company LTD."
        )
        @NotNull(message = "{company.null.validation.message}")
        @NotEmpty(message = "{company.empty.validation.message}")
        String company,

        @Schema(
                description = "Salary",
                example = "8000 PLN - 9000 PLN"
        )
        @NotNull(message = "{salary.null.validation.message}")
        @NotEmpty(message = "{salary.empty.validation.message}")
        String salary,

        @Schema(
                description = "URL to Job Offer to external web service.",
                example = "http://...."
        )
        @NotNull(message = "{url.null.validation.message}")
        @NotEmpty(message = "{url.empty.validation.message}")
        String offerUrl
) {
}
