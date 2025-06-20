package com.ebank.ebanking2.model.mapper;


import com.ebank.ebanking2.model.dto.RechargeDTO;
import com.ebank.ebanking2.model.dto.RechargeResDTO;
import com.ebank.ebanking2.model.entity.Recharge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RechargeMapper {

    @Mapping(target = "compte", ignore = true) //
    RechargeResDTO ToResDTO(Recharge recharge);

    List<RechargeResDTO> ToResDTOList(List<Recharge> recharges);

    @Mapping(target = "compte", ignore = true)
    Recharge ToEntity(RechargeDTO rechargeDTO);
}
