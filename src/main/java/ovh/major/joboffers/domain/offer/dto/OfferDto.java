package ovh.major.joboffers.domain.offer.dto;

public record OfferDto(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
