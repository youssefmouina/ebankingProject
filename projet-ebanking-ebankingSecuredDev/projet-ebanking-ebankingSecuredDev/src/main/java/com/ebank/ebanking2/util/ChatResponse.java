package com.ebank.ebanking2.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String text;
    private List<?> comptes;
    private String responseType; // "text" ou "comptes"

    // Constructeurs, getters et setters
}
