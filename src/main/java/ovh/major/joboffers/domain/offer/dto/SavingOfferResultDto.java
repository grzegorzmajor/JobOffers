package ovh.major.joboffers.domain.offer.dto;

public record SavingOfferResultDto(
        String title,
        String company,
        String salary,
        String offerUrl,
        boolean saved
) {
}
