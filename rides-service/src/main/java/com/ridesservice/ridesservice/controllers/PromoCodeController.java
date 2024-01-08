package com.ridesservice.ridesservice.controllers;

import com.ridesservice.ridesservice.dto.request.PromoCodeDTO;
import com.ridesservice.ridesservice.models.PromoCodesDTOList;
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
    public PromoCodeDTO getPromoCodeById(@RequestParam String name){
        return promoCodeService.getPromoCodeById(name);
    }
    @GetMapping("/all")
    public PromoCodesDTOList getAll() {
        return promoCodeService.getAll();
    }
    @PostMapping
    public PromoCodeDTO addPromoCode(@RequestBody @Valid PromoCodeDTO promoCodeDTO){
        return promoCodeService.addPromoCode(promoCodeDTO);
    }
    @PutMapping
    public PromoCodeDTO updatePromoCode(@RequestParam String name, @RequestBody @Valid PromoCodeDTO promoCodeDTO){
        return promoCodeService.updatePromoCode(name, promoCodeDTO);
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePromoCode(@RequestParam String name){
        return promoCodeService.deletePromoCode(name);
    }
}
