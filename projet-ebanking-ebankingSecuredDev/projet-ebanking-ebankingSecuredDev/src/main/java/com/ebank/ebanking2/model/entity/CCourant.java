package com.ebank.ebanking2.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("CCOURANT")
@Entity
public class CCourant extends Compte{
    private boolean autorisePaiementEnLigne=false;
    @OneToMany(mappedBy = "compteEmetteur",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Virement> virementsEmis=new ArrayList<>();
    @OneToMany(mappedBy = "compteRecepteur",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Virement> virementsRecu = new ArrayList<>();
    @OneToMany(mappedBy = "compte",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Recharge> recharges = new ArrayList<>();
}
