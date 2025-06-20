package com.ebank.ebanking2.model.mapper;

import com.ebank.ebanking2.model.dto.VirementDTO;
import com.ebank.ebanking2.model.dto.VirementDTOrib;
import com.ebank.ebanking2.model.dto.VirementResDTO;
import com.ebank.ebanking2.model.entity.Virement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface VirementMapper {
    @Mapping(target = "compteEmetteur", ignore = true)
    @Mapping(target = "compteRecepteur", ignore = true)
    public Virement toEntity(VirementDTO virementDTO);
    @Mapping(target = "compteEmetteur", ignore = true)
    @Mapping(target = "compteRecepteur", ignore = true)
    public Virement toEntity(VirementDTOrib virementDTOrib);
    @Mapping(target = "compteRecepteur.virementsEmis", ignore = true)
    @Mapping(target = "compteRecepteur.virementsRecu", ignore = true)
    @Mapping(target = "compteEmetteur.virementsEmis", ignore = true)
    @Mapping(target = "compteEmetteur.virementsRecu", ignore = true)
    @Mapping(target = "compteRecepteur.recharges", ignore = true)
    @Mapping(target = "compteEmetteur.recharges", ignore = true)
    public VirementResDTO toResDTO(Virement virement);
    List<VirementResDTO> toResDTOList(List<Virement> virements);
}
