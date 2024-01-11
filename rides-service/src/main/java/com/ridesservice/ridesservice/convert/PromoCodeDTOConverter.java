package com.ridesservice.ridesservice.convert;

import com.ridesservice.ridesservice.dto.request.PromoCodeRequest;
import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.PromoCodeResponse;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.models.PromoCode;
import com.ridesservice.ridesservice.models.Ride;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromoCodeDTOConverter {
    private final ModelMapper modelMapper;
    public PromoCodeResponse convertPromoCodeToPromoCodeResponse(PromoCode promoCode){
        return modelMapper.map(promoCode, PromoCodeResponse.class);
    }
    public PromoCode convertPromoCodeRequestToPromoCode(PromoCodeRequest promoCodeRequest){
        return modelMapper.map(promoCodeRequest,PromoCode.class);
    }
}
