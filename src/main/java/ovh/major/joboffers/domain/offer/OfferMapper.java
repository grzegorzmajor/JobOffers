package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDBResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferDBRequestDto;

public class OfferMapper {

    public static OfferDBResponseDto mapFromOfferToOfferDto(Offer offer) {
        return OfferDBResponseDto.builder()
                .id(offer.id())
                .position(offer.position())
                .company(offer.company())
                .offerUrl(offer.offerUrl())
                .salary(offer.salary())
                .build();
    }

    public static Offer mapFromExternalResponseDtoToOffer(OfferExternalResponseDto externalOffer) {
        return Offer.builder()
                .salary(externalOffer.salary())
                .company(externalOffer.company())
                .position(externalOffer.tittle())
                .offerUrl(externalOffer.offerUrl())
                .build();
    }
    public static Offer mapFromOfferRequestToOffer(OfferDBRequestDto offerRequest) {
        return Offer.builder()
                .company(offerRequest.company())
                .position(offerRequest.position())
                .offerUrl(offerRequest.offerUrl())
                .salary(offerRequest.salary())
                .build();
    }
}
