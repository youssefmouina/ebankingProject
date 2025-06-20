package com.ebank.ebanking2.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity

@Table(name = "comptes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)

public class Compte{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id") // client_id column here is mapped to Client
    @JsonBackReference
    private Client client;
    private String rib;
    private double solde;
    @Enumerated(EnumType.STRING)
    private StatusCompte status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    @Transient
    public String getAccountType() {
        return this.getClass().getSimpleName();
    }
    @Override
    public String toString() {
        return "Compte {" +
                "RIB='" + rib + '\'' +
                ", Solde=" + solde + "€" +
                ", Statut=" + status +
                ", Créé le=" + (createdAt != null ? createdAt.toLocalDate() : "N/A") +
                ", Mis à jour le=" + (updatedAt != null ? updatedAt.toLocalDate() : "N/A") +
                (deletedAt != null ? ", Supprimé le=" + deletedAt.toLocalDate() : "") +
                '}';
    }

}
