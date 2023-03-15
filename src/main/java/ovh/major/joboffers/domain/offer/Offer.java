package ovh.major.joboffers.domain.offer;

import lombok.Builder;

@Builder
record Offer(
        String title,
        String company,
        String salary,
        String offerUrl) {
}
