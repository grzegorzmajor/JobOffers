package ovh.major.joboffers.domain.offer;

import ovh.major.joboffers.domain.offer.dto.OfferDto;
import ovh.major.joboffers.domain.offer.dto.OfferExternalResponseDto;
import ovh.major.joboffers.domain.offer.dto.OfferRequestDto;

public class OfferMapper {

    public static OfferDto mapFromOfferToOfferDto(Offer offer) {
        return OfferDto.builder()
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
    public static Offer mapFromOfferRequestToOffer(OfferRequestDto offerRequest) {
        return Offer.builder()
                .company(offerRequest.company())
                .position(offerRequest.position())
                .offerUrl(offerRequest.offerUrl())
                .salary(offerRequest.salary())
                .build();
    }
}
