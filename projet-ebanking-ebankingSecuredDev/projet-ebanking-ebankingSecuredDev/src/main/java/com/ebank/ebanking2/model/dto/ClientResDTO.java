package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.Compte;
import com.ebank.ebanking2.model.entity.Invoice;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class ClientResDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String job;
    private boolean valid;
    private List<CompteResDTO> comptes;
    private List<InvoiceResDTO> invoices;
}
