package com.ebank.ebanking2.util;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class EcodeValidator {

    private static final Set<String> WEAK_CODES = Set.of(
            "0000", "1111", "2222", "3333", "4444", "5555",
            "6666", "7777", "8888", "9999", "1234", "4321"
    );



    public static boolean isSecureCode(String code) {

        // 1. Vérifie si le code est dans la liste noire
        if (WEAK_CODES.contains(code)) {
            return false;
        }

        // 2. Tous les chiffres sont identiques
        if (code.chars().distinct().count() == 1) {
            return false;
        }

        // 3. Vérifie les séquences ascendantes (ex: 1234)
        if (isSequential(code, true)) {
            return false;
        }

        // 4. Vérifie les séquences descendantes (ex: 4321)
        if (isSequential(code, false)) {
            return false;
        }

        return true; // Code considéré comme suffisamment sécurisé
    }


    private static boolean isSequential(String code, boolean ascending) {
        for (int i = 0; i < code.length() - 1; i++) {
            int current = code.charAt(i) - '0';
            int next = code.charAt(i + 1) - '0';
            if (ascending && next != current + 1) {
                return false;
            }
            if (!ascending && next != current - 1) {
                return false;
            }
        }
        return true;
    }


}
