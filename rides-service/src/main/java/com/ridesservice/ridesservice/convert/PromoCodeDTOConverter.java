package com.ridesservice.ridesservice.convert;

import com.ridesservice.ridesservice.dto.request.PromoCodeDTO;
import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.models.PromoCode;
import com.ridesservice.ridesservice.models.Ride;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromoCodeDTOConverter {
    private final ModelMapper modelMapper;
    public PromoCodeDTO convertPromoCodeToPromoCodeDTO(PromoCode promoCode){
        return modelMapper.map(promoCode, PromoCodeDTO.class);
    }
    public PromoCode convertPromoCodeDTOToPromoCode(PromoCodeDTO promoCodeDTO){
        return modelMapper.map(promoCodeDTO,PromoCode.class);
    }
}
