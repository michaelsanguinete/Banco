package br.com.banco.service;

import br.com.banco.dto.ContaResponse;
import br.com.banco.dto.SacarEDepositarRequest;
import br.com.banco.dto.TransferirRequest;
import br.com.banco.entity.Conta;
import br.com.banco.entity.Transferencia;
import br.com.banco.exception.SaldoInsuficienteException;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class ContaService {

    private final ContaRepository repository;
    private final TransferenciaRepository transferenciaRepository;
    private final ModelMapper mapper;

    public void sacar(SacarEDepositarRequest request){
        Conta conta = repository.getById(request.getId());
        if(conta.getSaldo().compareTo(request.getValor()) < 0) throw new SaldoInsuficienteException("Saldo insuficiente");
        BigDecimal valorAtualizado = conta.getSaldo().subtract(request.getValor());
        conta.setSaldo(valorAtualizado);
        repository.save(conta);

        Transferencia transferencia = new Transferencia(LocalDateTime.now(), request.getValor().multiply(BigDecimal.valueOf(-1)), "SAQUE", conta.getNomeResponsavel(), conta);
        transferenciaRepository.save(transferencia);
    }

    public void depositar(SacarEDepositarRequest request){
        Conta conta = repository.getById(request.getId());
        BigDecimal valorAtualizado = conta.getSaldo().add(request.getValor());
        conta.setSaldo(valorAtualizado);
        repository.save(conta);

        Transferencia transferencia = new Transferencia(LocalDateTime.now(), request.getValor(), "DEPOSITO", conta.getNomeResponsavel(), conta);
        transferenciaRepository.save(transferencia);
    }

    public void transferir(TransferirRequest request){
        Conta contaRemetente = repository.getById(request.getIdRemetente());
        if (contaRemetente.getSaldo().compareTo(request.getValor()) < 0) throw new SaldoInsuficienteException("Saldo insuficiente");
        Conta contaDestinatario = repository.getById(request.getIdDestinatario());

        BigDecimal valorAtualizadoRemetente = contaRemetente.getSaldo().subtract(request.getValor());
        BigDecimal valorAtualizadoDestinatario = contaDestinatario.getSaldo().add(request.getValor());

        contaRemetente.setSaldo(valorAtualizadoRemetente);
        contaDestinatario.setSaldo(valorAtualizadoDestinatario);

        repository.save(contaRemetente);
        repository.save(contaDestinatario);

        Transferencia transferenciaRemetente = new Transferencia(LocalDateTime.now(), request.getValor().multiply(BigDecimal.valueOf(-1)), "TRANSFERENCIA", contaRemetente.getNomeResponsavel(), contaRemetente);
        transferenciaRepository.save(transferenciaRemetente);

        Transferencia transferenciaDestinatario = new Transferencia(LocalDateTime.now(), request.getValor(), "TRANSFERENCIA", contaDestinatario.getNomeResponsavel(), contaDestinatario);
        transferenciaRepository.save(transferenciaDestinatario);
    }

    public ContaResponse retornaContaById(Long id){
        Conta conta = repository.getById(id);
        return mapper.map(conta, ContaResponse.class);
    }
}
