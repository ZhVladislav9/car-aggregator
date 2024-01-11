package com.ridesservice.ridesservice.service;

import com.ridesservice.ridesservice.convert.PromoCodeDTOConverter;
import com.ridesservice.ridesservice.dto.request.PromoCodeRequest;
import com.ridesservice.ridesservice.dto.response.PromoCodeListResponse;
import com.ridesservice.ridesservice.dto.response.PromoCodeResponse;
import com.ridesservice.ridesservice.exceptions.AlreadyExistsException;
import com.ridesservice.ridesservice.exceptions.InvalidRequestException;
import com.ridesservice.ridesservice.exceptions.PromoCodeNotFoundException;
import com.ridesservice.ridesservice.models.PromoCode;
import com.ridesservice.ridesservice.repository.PromoCodeRepository;
import com.ridesservice.ridesservice.service.interfaces.PromoCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ridesservice.ridesservice.util.Messages.*;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeDTOConverter promoCodeDTOConverter;

    public Double getCoefficientByPromoCodeName(String name){
        if(!promoCodeRepository.existsByName(name)){
            throw new PromoCodeNotFoundException(
                    String.format(PROMO_CODE_NOT_FOUND_WITH_NAME_MESSAGE, name));
        }
        return promoCodeDTOConverter
                .convertPromoCodeToPromoCodeResponse(promoCodeRepository.findByName(name))
                .getCoefficient();
    }
    public PromoCodeResponse getPromoCodeById(Integer id){
        return promoCodeDTOConverter
                .convertPromoCodeToPromoCodeResponse(promoCodeRepository.findById(id)
                        .orElseThrow(() -> new PromoCodeNotFoundException(
                                String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id))));
    }
    public PromoCodeListResponse getAll(){
        List<PromoCodeResponse> codes = promoCodeRepository.findAll()
                .stream()
                .map(promoCodeDTOConverter::convertPromoCodeToPromoCodeResponse)
                .toList();
        return PromoCodeListResponse.builder()
                .codes(codes)
                .build();
    }
    @Transactional
    public PromoCodeResponse addPromoCode(PromoCodeRequest promoCodeRequest){
        dataIsUniqueCheck(promoCodeRequest.getName());
        return promoCodeDTOConverter.convertPromoCodeToPromoCodeResponse(
                promoCodeRepository
                        .save(promoCodeDTOConverter
                                .convertPromoCodeRequestToPromoCode(promoCodeRequest)));
    }
    @Transactional
    public ResponseEntity<HttpStatus> deletePromoCode(Integer id) {
        checkExistence(id);
        promoCodeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public PromoCodeResponse updatePromoCode(Integer id, PromoCodeRequest promoCodeRequest){
        if(!promoCodeRepository.existsById(id))
            throw new PromoCodeNotFoundException(String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id));
        PromoCode promoCode = promoCodeDTOConverter.convertPromoCodeRequestToPromoCode(promoCodeRequest);
        nameUpdateCheck(id, promoCodeRequest);
        promoCode.setId(id);
        promoCodeRepository.save(promoCode);
        return promoCodeDTOConverter.convertPromoCodeToPromoCodeResponse(promoCode);
    }
    private void nameUpdateCheck(Integer id, PromoCodeRequest promoCodeRequest){
        PromoCode promoCode = getPromoCodeEntityById(id);
        if(!promoCode.getName().equals(promoCodeRequest.getName()))
            dataIsUniqueCheck(promoCodeRequest.getName());
    }
    private void dataIsUniqueCheck(String name) {
        if (promoCodeRepository.existsByName(name)) {
            throw new AlreadyExistsException(
                    String.format(PROMO_CODE_WITH_NAME_EXISTS_MESSAGE, name));
        }
    }
    private void checkExistence(Integer id) {
        if (!promoCodeRepository.existsById(id)) {
            throw new InvalidRequestException("PromoCode notFound");
        }
    }
    private PromoCode getPromoCodeEntityById(Integer id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new PromoCodeNotFoundException(
                        String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id)));
    }
}
