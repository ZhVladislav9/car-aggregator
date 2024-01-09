package com.ridesservice.ridesservice.service;

import com.ridesservice.ridesservice.convert.PromoCodeDTOConverter;
import com.ridesservice.ridesservice.dto.request.PromoCodeDTO;
import com.ridesservice.ridesservice.exceptions.AlreadyExistsException;
import com.ridesservice.ridesservice.exceptions.InvalidRequestException;
import com.ridesservice.ridesservice.exceptions.PromoCodeNotFoundException;
import com.ridesservice.ridesservice.models.PromoCode;
import com.ridesservice.ridesservice.models.PromoCodesDTOList;
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

    public Double getCoefficientByPromoCodeId(String id){
        PromoCodeDTO promoCodeDTO = getPromoCodeById(id);
        return promoCodeDTO.getCoefficient();
    }
    public PromoCodeDTO getPromoCodeById(String id){
        return promoCodeDTOConverter
                .convertPromoCodeToPromoCodeDTO(promoCodeRepository.findById(id)
                        .orElseThrow(() -> new PromoCodeNotFoundException(
                                String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id))));
    }
    public PromoCodesDTOList getAll(){
        List<PromoCodeDTO> codes = promoCodeRepository.findAll()
                .stream()
                .map(promoCodeDTOConverter::convertPromoCodeToPromoCodeDTO)
                .toList();
        return PromoCodesDTOList.builder()
                .codes(codes)
                .build();
    }
    @Transactional
    public PromoCodeDTO addPromoCode(PromoCodeDTO promoCodeDTO){
        dataIsUniqueCheck(promoCodeDTO.getName());
        promoCodeRepository.save(promoCodeDTOConverter.convertPromoCodeDTOToPromoCode(promoCodeDTO));
        return promoCodeDTO;
    }
    @Transactional
    public ResponseEntity<HttpStatus> deletePromoCode(String name) {
        checkExistence(name);
        promoCodeRepository.deleteById(name);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public PromoCodeDTO updatePromoCode(String id, PromoCodeDTO promoCodeDTO){
        if(!promoCodeRepository.existsById(id))
            throw new PromoCodeNotFoundException(String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id));
        PromoCode promoCode = promoCodeDTOConverter.convertPromoCodeDTOToPromoCode(promoCodeDTO);
        nameUpdateCheck(id, promoCodeDTO);
        promoCodeRepository.save(promoCode);
        return promoCodeDTO;
    }
    private void nameUpdateCheck(String id, PromoCodeDTO promoCodeDTO){
        PromoCode promoCode = getPromoCodeEntityById(id);
        if(!promoCode.getName().equals(promoCode.getName()))
            dataIsUniqueCheck(promoCodeDTO.getName());
    }
    private void dataIsUniqueCheck(String name) {
        if (promoCodeRepository.existsById(name)) {
            throw new AlreadyExistsException(
                    String.format(PROMO_CODE_WITH_NAME_EXISTS_MESSAGE, name));
        }
    }
    private void checkExistence(String name) {
        if (!promoCodeRepository.existsById(name)) {
            throw new InvalidRequestException("PromoCode notFound");
        }
    }
    private PromoCode getPromoCodeEntityById(String id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new PromoCodeNotFoundException(String.format(PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE, id)));
    }
}
