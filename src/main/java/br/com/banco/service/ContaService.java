package br.com.banco.service;

import br.com.banco.dto.ContaResponse;
import br.com.banco.dto.TransferenciaResponse;
import br.com.banco.entity.Conta;
import br.com.banco.repository.ContaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ContaService {

    private final ContaRepository repository;
    private final ModelMapper mapper;

    public void sacar(Long id, BigDecimal valor){
        Conta conta = repository.getById(id);
        BigDecimal valorAtualizado = conta.getSaldo().subtract(valor);
        conta.setSaldo(valorAtualizado);
        repository.save(conta);
    }

    public void depositar(Long id, BigDecimal valor){
        Conta conta = repository.getById(id);
        BigDecimal valorAtualizado = conta.getSaldo().add(valor);
        conta.setSaldo(valorAtualizado);
        repository.save(conta);
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
    }

    public ContaResponse retornaContaById(Long id){
        Conta conta = repository.getById(id);
        return mapper.map(conta, ContaResponse.class);
    }
}
