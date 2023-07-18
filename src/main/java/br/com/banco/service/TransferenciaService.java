package br.com.banco.service;

import br.com.banco.dto.TransferenciaResponse;
import br.com.banco.entity.Transferencia;
import br.com.banco.repository.TransferenciaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TransferenciaService {

    private final TransferenciaRepository repository;
    private final ModelMapper mapper;

    public List<TransferenciaResponse> retornaTransferenciaByContaId(Long contaId){
        List<Transferencia> transferencias = repository.findByContaId(contaId);
        return transferencias.stream()
                .map(transferencia -> mapper.map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> retornaTransferencias(){
        List<Transferencia> transferencias = repository.findAll();
        return transferencias.stream()
                .map(transferencia -> mapper.map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> retornaTransferenciaByDatas(LocalDateTime dataInicial, LocalDateTime dataFinal){
        List<Transferencia> transferencias = repository.findByDataTransferenciaBetween(dataInicial, dataFinal);
        return transferencias.stream()
                .map(transferencia -> mapper.map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> findByNomeOperadorTransacao(String nomeOperadorTransacao){
        List<Transferencia> transferencias = repository.findByNomeOperadorTransacao(nomeOperadorTransacao);
        return transferencias.stream()
                .map(transferencia -> mapper.map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> retornaTransferenciaComFiltros(Long contaId, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperadorTransacao) {
        List<Transferencia> transferencias = repository.findAllByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(contaId, dataInicial, dataFinal, nomeOperadorTransacao);
        return transferencias.stream()
                .map(transferencia -> mapper.map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());
    }


}
