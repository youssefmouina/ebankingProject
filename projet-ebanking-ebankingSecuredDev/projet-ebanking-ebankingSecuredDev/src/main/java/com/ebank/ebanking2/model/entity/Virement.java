package com.ebank.ebanking2.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Virement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) //@ManyToOne means many records of the current entity are associated with one record of another entity
    @JoinColumn(name = "compte_emetteur_id")
    @JsonManagedReference
    private CCourant compteEmetteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "compte_recepteur_id")
    @JsonManagedReference
    private CCourant compteRecepteur;

    private double montant;

    @Enumerated(EnumType.STRING)
    private Type type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


}
