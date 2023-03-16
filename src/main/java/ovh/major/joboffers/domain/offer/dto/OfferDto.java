package ovh.major.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferDto(
        String id,
        String position,
        String company,
        String salary,
        String offerUrl
) {
}
