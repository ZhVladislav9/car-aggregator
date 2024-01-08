package com.ridesservice.ridesservice.models;

import com.ridesservice.ridesservice.dto.request.PromoCodeDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodesDTOList {
    List<PromoCodeDTO> codes;
}