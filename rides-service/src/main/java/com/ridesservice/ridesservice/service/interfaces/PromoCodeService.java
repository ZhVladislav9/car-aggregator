package com.ridesservice.ridesservice.service.interfaces;

import com.ridesservice.ridesservice.dto.request.PromoCodeDTO;
import com.ridesservice.ridesservice.models.PromoCodesDTOList;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface PromoCodeService {
    public PromoCodeDTO getPromoCodeById(String id);
    public PromoCodesDTOList getAll();
    public PromoCodeDTO addPromoCode(PromoCodeDTO promoCodeDTO);
    public ResponseEntity<HttpStatus> deletePromoCode(String name);
    public PromoCodeDTO updatePromoCode(String id, PromoCodeDTO promoCodeDTO);
}
