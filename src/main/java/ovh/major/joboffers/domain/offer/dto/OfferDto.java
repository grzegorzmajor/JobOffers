package ovh.major.joboffers.domain.offer.dto;

public record OfferDto(
        String id,
        String position,
        String company,
        String salary,
        String offerUrl
) {
}
