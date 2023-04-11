package ovh.major.joboffers.domain.offer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OfferDBRequestDto(

        @NotNull(message = "position should be not null")
        @NotEmpty(message = "position should be not empty")
        String position,

        @NotNull(message = "company should be not null")
        @NotEmpty(message = "company should be not empty")
        String company,

        @NotNull(message = "salary should be not null")
        @NotEmpty(message = "salary should be not empty")
        String salary,

        @NotNull(message = "offer url should be not null")
        @NotEmpty(message = "offer url should be not empty")
        String offerUrl
) {
}
