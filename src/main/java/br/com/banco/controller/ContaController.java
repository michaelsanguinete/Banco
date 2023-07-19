package br.com.banco.controller;

import br.com.banco.dto.ContaResponse;
import br.com.banco.dto.SacarEDepositarRequest;
import br.com.banco.dto.TransferirRequest;
import br.com.banco.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
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

    @PutMapping("/depositar")
    @Transactional
    public ResponseEntity<ContaResponse> deposita(@RequestBody @Valid SacarEDepositarRequest request){
        service.depositar(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/sacar")
    @Transactional
    public ResponseEntity<ContaResponse> saca(@RequestBody @Valid SacarEDepositarRequest resquest){
        service.sacar(resquest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/transferir")
    @Transactional
    public ResponseEntity<ContaResponse> transfere(@RequestBody @Valid TransferirRequest request){
        service.transferir(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ContaResponse> deletaConta(@PathVariable Long id){
        service.deletaConta(id);
        return ResponseEntity.ok().build();
    }

}
