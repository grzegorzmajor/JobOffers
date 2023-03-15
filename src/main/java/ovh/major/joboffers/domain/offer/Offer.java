package ovh.major.joboffers.domain.offer;

import lombok.Builder;

@Builder
public record Offer(
        String title,
        String company,
        String salary,
        String offerUrl) {
}
