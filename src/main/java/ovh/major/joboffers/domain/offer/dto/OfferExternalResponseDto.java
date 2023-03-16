package ovh.major.joboffers.domain.offer.dto;

public record OfferExternalResponseDto(
    String tittle,
    String company,
    String salary,
    String offerUrl
) {
}
