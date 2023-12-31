package br.com.banco.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @Column(name = "nome_responsavel", nullable = false)
    private String nomeResponsavel;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal saldo = new BigDecimal(BigInteger.ZERO);
}
