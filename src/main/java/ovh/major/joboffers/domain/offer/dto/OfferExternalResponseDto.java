package ovh.major.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferExternalResponseDto(
        String tittle,
        String company,
        String salary,
        String offerUrl
) {
}
