package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;

import java.util.List;

public interface OfferFetchable {
    List<OfferExternalResponseDto> fetchOffers();
}
