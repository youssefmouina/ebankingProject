package com.ebank.ebanking2.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cryptodtoresacheter {
  long id;
    private  String namecrypto;
    private  double valueacheter;



    private long ccourant;
}
