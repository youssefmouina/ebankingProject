package com.ebank.ebanking2.model.mapper;

import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.CCourant;
import com.ebank.ebanking2.model.entity.CEpargne;
import com.ebank.ebanking2.model.entity.Compte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses={VirementMapper.class,RechargeMapper.class})
public interface CompteMapper {
    @Mapping(target = "client", ignore = true)
    CCourant toEntity(CCourantDTO cCourantDTO);
    @Mapping(target = "client", ignore = true)
    CCourantResDTO toResDTO(CCourant cCourant);
    @Mapping(target = "client", ignore = true) // client is set manually in service
    CEpargne toEntity(CEpargneDTO cEpargneDTO);
    CEpargneResDTO toResDTO(CEpargne cEpargne);
    @Mapping(target = "accountType", expression = "java(compte.getAccountType())")
    CompteResDTO toCompteResDTO(Compte compte);
    CCourantDTO toDtoCoutant(CCourant courant);

    CompteResDTOChat toCompteResDTOChat(Compte compte);
}
