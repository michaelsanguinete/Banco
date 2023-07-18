package br.com.banco.controller;

import br.com.banco.dto.ContaResponse;
import br.com.banco.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/conta")
public class ContaController {

    private final ContaService service;

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> retornaContaById(@PathVariable Long id){
        return ResponseEntity.ok(service.retornaContaById(id));
    }

    @PutMapping("/depositar/{id}/{valor}")
    public ResponseEntity<ContaResponse> deposita(@PathVariable Long id, @PathVariable BigDecimal valor){
        service.depositar(id, valor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/sacar/{id}/{valor}")
    public ResponseEntity<ContaResponse> saca(@PathVariable Long id, @PathVariable BigDecimal valor){
        service.sacar(id, valor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/transferir/{idRemetente}/{idDestinatario}/{valor}")
    public ResponseEntity<ContaResponse> transfere(@PathVariable Long idRemetente, @PathVariable Long idDestinatario, @PathVariable BigDecimal valor){
        service.transferir(idRemetente, idDestinatario, valor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
