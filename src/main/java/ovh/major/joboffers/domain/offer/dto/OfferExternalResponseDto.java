package ovh.major.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferExternalResponseDto(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
