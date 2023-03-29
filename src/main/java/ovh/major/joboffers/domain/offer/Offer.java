package ovh.major.joboffers.domain.offer;

import lombok.Builder;

@Builder
public
record Offer(
        String id,
        String position,
        String company,
        String salary,
        String offerUrl) {
}
