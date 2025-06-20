package com.ebank.ebanking2.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RibGenerator {

    private String PREFIX="010101";
    private String SUFFIX="01";


    public String generateNextId(String lastRib) {
        if (lastRib == null || lastRib.isEmpty()) {
            return PREFIX + String.format("%0" + 16 + "d", 1)+SUFFIX;
        }
        int start = PREFIX.length();
        int end = lastRib.length() - SUFFIX.length();
        String numericPart = lastRib.substring(start, end);
        long number = Long.parseLong(numericPart) + 1;
        return PREFIX + String.format("%0" + 16 + "d", number)+SUFFIX;
    }

}