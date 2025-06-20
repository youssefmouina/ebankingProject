package com.ebank.ebanking2.model.mapper;

import com.ebank.ebanking2.model.dto.ClientDTO;
import com.ebank.ebanking2.model.dto.ClientResDTO;
import com.ebank.ebanking2.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "comptes", ignore = true)
    Client toEntity(ClientDTO clientdto);
    ClientResDTO toResDTO(Client client);
}
