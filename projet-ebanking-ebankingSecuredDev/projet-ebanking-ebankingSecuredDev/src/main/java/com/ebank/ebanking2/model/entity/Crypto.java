package com.ebank.ebanking2.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity

@Table(name = "crypto")
public class Crypto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String namecrypto;
    private  double valueacheter;
    private  double valuevendre;


    @ManyToOne
    @JoinColumn(name = "ccourant_id", referencedColumnName = "id")
    private  CCourant ccourant;

}
