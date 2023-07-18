package br.com.banco.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_transferencia", nullable = false)
    private LocalDateTime dataTransferencia;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 15)
    private String tipo;

    @Column(name = "nome_operador_transacao", length = 50)
    private String nomeOperadorTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;
}
