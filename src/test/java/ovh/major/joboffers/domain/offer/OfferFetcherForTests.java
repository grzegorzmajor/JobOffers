package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;

import java.util.List;

public class OfferFetcherForTests implements OfferFetchable {

    List<OfferExternalResponseDto> listOfOffers;

    OfferFetcherForTests(List<OfferExternalResponseDto> listOfOffers) {
        this.listOfOffers = listOfOffers;
    }

    @Override
    public List<OfferExternalResponseDto> fetchOffers() {
        return listOfOffers;
    }
}
