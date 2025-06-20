package com.ebank.ebanking2.Service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public interface CompteTools {
    @Tool("Récupère le solde d'un compte à partir de son RIB")
    double getSoldee(@P("Le RIB du compte") String rib);
}
