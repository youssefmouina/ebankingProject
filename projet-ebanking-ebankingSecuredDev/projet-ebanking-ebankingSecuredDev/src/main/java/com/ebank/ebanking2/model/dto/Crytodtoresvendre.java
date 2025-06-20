package com.ebank.ebanking2.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crytodtoresvendre{
    private Long id;
    private  String namecrypto;
    private  double valuevendre;



    private long ccourant;
}
