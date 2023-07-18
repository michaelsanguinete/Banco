package br.com.banco.service;

import br.com.banco.dto.ContaResponse;
import br.com.banco.dto.TransferenciaResponse;
import br.com.banco.entity.Conta;
import br.com.banco.entity.Transferencia;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ContaService {

    private final ContaRepository repository;
    private final TransferenciaRepository transferenciaRepository;
    private final ModelMapper mapper;

    public void sacar(Long id, BigDecimal valor){
        Conta conta = repository.getById(id);
        BigDecimal valorAtualizado = conta.getSaldo().subtract(valor);
        conta.setSaldo(valorAtualizado);
        repository.save(conta);

        Transferencia transferencia = new Transferencia(LocalDateTime.now(), valor.multiply(BigDecimal.valueOf(-1)), "SAQUE", conta.getNomeResponsavel(), conta);
        transferenciaRepository.save(transferencia);
    }

    public void depositar(Long id, BigDecimal valor){
        Conta conta = repository.getById(id);
        BigDecimal valorAtualizado = conta.getSaldo().add(valor);
        conta.setSaldo(valorAtualizado);
        repository.save(conta);

        Transferencia transferencia = new Transferencia(LocalDateTime.now(), valor, "DEPOSITO", conta.getNomeResponsavel(), conta);
        transferenciaRepository.save(transferencia);
    }

    public void transferir(Long idRemetente, Long idDestinatario, BigDecimal valor){
        Conta contaRemetente = repository.getById(idRemetente);
        Conta contaDestinatario = repository.getById(idDestinatario);

        BigDecimal valorAtualizadoRemetente = contaRemetente.getSaldo().subtract(valor);
        BigDecimal valorAtualizadoDestinatario = contaDestinatario.getSaldo().add(valor);

        contaRemetente.setSaldo(valorAtualizadoRemetente);
        contaDestinatario.setSaldo(valorAtualizadoDestinatario);

        repository.save(contaRemetente);
        repository.save(contaDestinatario);

        Transferencia transferenciaRemetente = new Transferencia(LocalDateTime.now(), valor.multiply(BigDecimal.valueOf(-1)), "TRANSFERENCIA", contaRemetente.getNomeResponsavel(), contaRemetente);
        transferenciaRepository.save(transferenciaRemetente);

        Transferencia transferenciaDestinatario = new Transferencia(LocalDateTime.now(), valor, "TRANSFERENCIA", contaDestinatario.getNomeResponsavel(), contaDestinatario);
        transferenciaRepository.save(transferenciaDestinatario);
    }

    public ContaResponse retornaContaById(Long id){
        Conta conta = repository.getById(id);
        return mapper.map(conta, ContaResponse.class);
    }
}
