package ovh.major.joboffers.domain.offer.dto;

public record SavingOfferResultDto(
        String id,
        String position,
        String company,
        String salary,
        String offerUrl,
        boolean saved
) {
}
