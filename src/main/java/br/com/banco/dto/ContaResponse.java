package br.com.banco.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ContaResponse {

    private String nomeResponsavel;
    private BigDecimal saldo;
}
