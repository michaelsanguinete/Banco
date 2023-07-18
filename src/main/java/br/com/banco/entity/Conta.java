package br.com.banco.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @Column(name = "nome_responsavel", nullable = false)
    private String nomeResponsavel;
}
