package ovh.major.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferRequestDto(
        String position,
        String company,
        String salary,
        String offerUrl
) {
}
