package com.ebank.ebanking2.model.mapper;


import com.ebank.ebanking2.model.dto.InvoiceDTO;
import com.ebank.ebanking2.model.dto.InvoiceResDTO;
import com.ebank.ebanking2.model.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface InvoiceMapper {

    @Mapping(target = "client", ignore = true) // Ignore client; set it manually later
    Invoice toEntity(InvoiceDTO invoiceDto);
    InvoiceResDTO toResDto(Invoice invoice);
    List<Invoice> toEntity(List<InvoiceDTO> invoiceDTOs);
    List<InvoiceResDTO> toResDTO(List<Invoice> invoices);
}
