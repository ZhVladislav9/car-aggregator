package com.ridesservice.ridesservice.controllers;

import com.ridesservice.ridesservice.dto.request.PromoCodeRequest;
import com.ridesservice.ridesservice.dto.response.PromoCodeListResponse;
import com.ridesservice.ridesservice.dto.response.PromoCodeResponse;
import com.ridesservice.ridesservice.service.PromoCodeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promo-code")
@RequiredArgsConstructor

public class PromoCodeController {
    private final PromoCodeServiceImpl promoCodeService;

    @GetMapping
    public PromoCodeResponse getPromoCodeById(@RequestParam Integer id){
        return promoCodeService.getPromoCodeById(id);
    }
    @GetMapping("/all")
    public PromoCodeListResponse getAll() {
        return promoCodeService.getAll();
    }
    @PostMapping
    public PromoCodeResponse addPromoCode(@RequestBody @Valid PromoCodeRequest promoCodeRequest){
        return promoCodeService.addPromoCode(promoCodeRequest);
    }
    @PutMapping
    public PromoCodeResponse updatePromoCode(@RequestParam Integer id, @RequestBody @Valid PromoCodeRequest promoCodeRequest){
        return promoCodeService.updatePromoCode(id, promoCodeRequest);
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePromoCode(@RequestParam Integer id){
        return promoCodeService.deletePromoCode(id);
    }
}
