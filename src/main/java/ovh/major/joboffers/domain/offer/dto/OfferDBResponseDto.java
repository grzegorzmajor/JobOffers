package ovh.major.joboffers.domain.offer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Schema(
        description = "The object returned by access methods to the database."
)
@Builder
public record OfferDBResponseDto(

        @Schema(
                description = "Offer id in database."
        )
        String id,

        @Schema(
                description = "Position",
                example = "Junior Java Developer"
        )
        String position,

        @Schema(
                description = "Company name",
                example = "Junior Java Developer Development Company LTD."
        )
        String company,

        @Schema(
                description = "Salary",
                example = "8000 PLN - 9000 PLN"
        )
        String salary,

        @Schema(
                description = "URL to Job Offer to external web service.",
                example = "http://...."
        )
        String offerUrl
) implements Serializable {
}
