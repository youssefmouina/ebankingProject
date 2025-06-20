package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.VirementDTOrib;
import com.ebank.ebanking2.model.dto.VirementResDTO;
import com.ebank.ebanking2.model.entity.Compte;
import com.ebank.ebanking2.model.entity.StatusCompte;
import com.ebank.ebanking2.model.entity.Type;
import com.ebank.ebanking2.model.entity.Virement;
import com.ebank.ebanking2.repository.CompteRepo;
import com.ebank.ebanking2.repository.VirementRepo;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BankingTools {

    @Autowired
    CompteRepo compteRepo;
    @Autowired
    VirementRepo virementRepo;
    @Autowired
    VirementService virementService;

//    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    @Tool("Obtenir les soldes des comptes bancaires de l'utilisateur. Utilise cette fonction quand l'utilisateur demande son solde, ses soldes, ou le montant sur ses comptes.")
    public String getUserSolde(@P("userId") @org.springframework.security.access.method.P("userId") Long userId) {
        System.out.println("solde");
        List<Compte> comptes = compteRepo.findByClientIdAndStatus(userId, StatusCompte.ACTIF);
        if (comptes.isEmpty()) {
            return "Sorry, we couldn't find any active accounts for your ID.";
        }
        return comptes.stream()
                .map(compte -> compte.getRib() + ": " + compte.getSolde() + " MAD")
                .collect(Collectors.joining("\n"));
}

//    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    @Tool("List user's accounts")
    public String listUserAccounts(@P("userId") Long userId) {
        System.out.println("accounts");
        List<Compte> comptes = compteRepo.findByClientId(userId);
        return comptes.stream()
                .map(compte -> compte.getRib())
                .collect(Collectors.joining("\n"));
    }

//    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    @Tool("Get user's transaction history")
    public String getTransactionHistory(@P("userId") Long userId) {
        System.out.println("history");

        Optional<List<Virement>> optionalVirements = virementRepo.getLastTransactions(userId);

        if (optionalVirements.isEmpty() || optionalVirements.get().isEmpty()) {
            return "Aucune transaction trouvée pour cet utilisateur.";
        }

        List<Virement> virements = optionalVirements.get();

        return virements.stream()
                .limit(10) // Limite à 10 transactions récentes
                .map(v -> {
                    String type = v.getCompteEmetteur().getClient().getId().equals(userId) ? "Envoyé à" : "Reçu de";
                    String ribAutre = v.getCompteEmetteur().getClient().getId().equals(userId)
                            ? v.getCompteRecepteur().getRib()
                            : v.getCompteEmetteur().getRib();
                    return String.format("%s %s | %.2f MAD | %s",
                            type,
                            ribAutre,
                            v.getMontant(),
                            v.getCreatedAt().toString());
                })
                .collect(Collectors.joining("\n"));
    }

//    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteRib(#ribFrom).id == authentication.principal.id)")
    @Tool("Make a transfer")
    public String makeTransfer(@P("fromAccount") @org.springframework.security.access.method.P("ribFrom") String ribFrom,
                               @P("toAccount") String ribTo,
                               @P("amount") double amount) {
        System.out.println("transfert");

        try {
            VirementDTOrib dto = new VirementDTOrib();
            dto.setCompteEmetteur(ribFrom);
            dto.setCompteRecepteur(ribTo);
            dto.setMontant(amount);
            dto.setType(Type.INSTANTANEE); //TODO:ask the user about the transaction type

            VirementResDTO response = virementService.executeVirement(dto);

            return String.format("✅ Virement effectué avec succès : %.2f MAD de %s vers %s à %s.",
                    amount, ribFrom, ribTo, response.getCreatedAt());
        } catch (IllegalArgumentException e) {
            return "❌ Erreur de validation : " + e.getMessage();
        } catch (IOException e) {
            return "❌ Erreur lors de la génération du reçu PDF.";
        } catch (Exception e) {
            return "❌ Une erreur inattendue est survenue : " + e.getMessage();
        }
    }
}