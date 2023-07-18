package br.com.banco.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferirRequest {

    @NotNull
    private Long idRemetente;

    @NotNull
    private Long idDestinatario;

    @NotNull @DecimalMin("0.01")
    private BigDecimal valor;
}
