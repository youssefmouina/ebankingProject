package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.CCourant;
import com.ebank.ebanking2.model.entity.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VirementResDTO {
    private Long id;
    private CompteResDTO compteEmetteur;
    private CompteResDTO compteRecepteur;
    private double montant;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private Type type;
}
