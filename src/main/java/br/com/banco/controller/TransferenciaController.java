package br.com.banco.controller;

import br.com.banco.dto.TransferenciaResponse;
import br.com.banco.service.TransferenciaService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/transferencia")
public class TransferenciaController {

    private final TransferenciaService service;

    @GetMapping("/{contaId}")
    public ResponseEntity<List<TransferenciaResponse>> retornaTransferenciaByContaId(@PathVariable Long contaId){
        return ResponseEntity.ok(service.retornaTransferenciaByContaId(contaId));
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaResponse>> retornaTransferencias(){
        return ResponseEntity.ok(service.retornaTransferencias());
    }

    @GetMapping("/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<TransferenciaResponse>> retornaTransferenciaByDatas(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal){
        return ResponseEntity.ok(service.retornaTransferenciaByDatas(dataInicial, dataFinal));
    }

    @GetMapping("/nome/{nomeOperadorTransacao}")
    public ResponseEntity<List<TransferenciaResponse>> findByNomeOperadorTransacao(@PathVariable String nomeOperadorTransacao){
        return ResponseEntity.ok(service.findByNomeOperadorTransacao(nomeOperadorTransacao));
    }

    @GetMapping("/{contaId}/{dataInicial}/{dataFinal}/{nomeOperadorTransacao}")
    public ResponseEntity<List<TransferenciaResponse>> retornaTransferenciaComFiltros(@PathVariable Long contaId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal, @PathVariable String nomeOperadorTransacao){
        return ResponseEntity.ok(service.retornaTransferenciaComFiltros(contaId,dataInicial,dataInicial,nomeOperadorTransacao));
    }
}
