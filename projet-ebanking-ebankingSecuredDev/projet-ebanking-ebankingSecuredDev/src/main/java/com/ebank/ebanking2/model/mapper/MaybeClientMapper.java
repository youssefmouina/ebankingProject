package com.ebank.ebanking2.model.mapper;

import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.MaybeClient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaybeClientMapper {
    MaybeClient toEntity(MaybeClientDTO maybeClientDTO);
    MaybeClientDTO toDto(MaybeClient maybeClient);
    MaybeClient toEntity(MaybeClientResDTO maybeClientResDTO);
    MaybeClientResDTO toResDto(MaybeClient maybeClient);
    MaybeClient toEntity(MaybeClientWithEmailTokenDTO maybeClientWithEmailTokenDTO);
    SubMaybeClientDTO toDto(ClientDTO clientDTO);
}
