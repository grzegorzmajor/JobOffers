package ovh.major.joboffers.infrastructure.offer.controler;

import lombok.Builder;

@Builder
public record RequestDataDto(
        boolean offersFilter
) {
}
