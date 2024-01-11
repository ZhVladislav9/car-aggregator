package com.ridesservice.ridesservice.service.interfaces;

import com.ridesservice.ridesservice.dto.request.PromoCodeRequest;
import com.ridesservice.ridesservice.dto.response.PromoCodeListResponse;
import com.ridesservice.ridesservice.dto.response.PromoCodeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PromoCodeService {
    public PromoCodeResponse getPromoCodeById(Integer id);
    public PromoCodeListResponse getAll();
    public PromoCodeResponse addPromoCode(PromoCodeRequest promoCodeRequest);
    public ResponseEntity<HttpStatus> deletePromoCode(Integer id);
    public PromoCodeResponse updatePromoCode(Integer id, PromoCodeRequest promoCodeRequest);
}
