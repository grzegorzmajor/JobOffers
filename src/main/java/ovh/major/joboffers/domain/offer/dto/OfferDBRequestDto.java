package ovh.major.joboffers.domain.offer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OfferDBRequestDto(

        @NotNull(message = "{position.null.validation.message}")
        @NotEmpty(message = "{position.empty.validation.message}")
        String position,

        @NotNull(message = "{company.null.validation.message}")
        @NotEmpty(message = "{company.empty.validation.message}")
        String company,

        @NotNull(message = "{salary.null.validation.message}")
        @NotEmpty(message = "{salary.empty.validation.message}")
        String salary,

        @NotNull(message = "{url.null.validation.message}")
        @NotEmpty(message = "{url.empty.validation.message}")
        String offerUrl
) {
}
