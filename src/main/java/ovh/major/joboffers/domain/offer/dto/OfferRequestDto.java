package ovh.major.joboffers.domain.offer.dto;

public record OfferRequestDto(
        String position,
        String company,
        String salary,
        String offerUrl
) {
}
