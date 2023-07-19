package br.com.banco.service;

import br.com.banco.dto.TransferenciaResponse;
import br.com.banco.entity.Conta;
import br.com.banco.entity.Transferencia;
import br.com.banco.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransferenciaServiceTest {

    private TransferenciaService transferenciaService;

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        transferenciaService = new TransferenciaService(transferenciaRepository, modelMapper);
    }

    @Test
    public void testRetornaTransferenciaByContaId() {
        Long contaId = 1L;
        Transferencia transferencia1 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(100), "DEPOSITO", "Fulano", new Conta());
        Transferencia transferencia2 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(200), "DEPOSITO", "De Tal", new Conta());
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);
        when(transferenciaRepository.findByContaId(contaId)).thenReturn(transferencias);

        List<TransferenciaResponse> expectedResponses = transferencias.stream()
                .map(transferencia -> new ModelMapper().map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());

        List<TransferenciaResponse> actualResponses = transferenciaService.retornaTransferenciaByContaId(contaId);

        assertEquals(expectedResponses, actualResponses);
    }

    @Test
    public void testRetornaTransferencias() {
        Transferencia transferencia1 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(100), "DEPOSITO", "Fulano", new Conta());
        Transferencia transferencia2 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(200), "DEPOSITO", "De Tal", new Conta());
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);
        when(transferenciaRepository.findAll()).thenReturn(transferencias);

        List<TransferenciaResponse> expectedResponses = transferencias.stream()
                .map(transferencia -> new ModelMapper().map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());

        List<TransferenciaResponse> actualResponses = transferenciaService.retornaTransferencias();

        assertEquals(expectedResponses, actualResponses);
    }

    @Test
    public void testRetornaTransferenciaByDatas() {
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusDays(1);
        Transferencia transferencia1 = new Transferencia(dataInicial, BigDecimal.valueOf(100), "DEPOSITO", "Fulano", new Conta());
        Transferencia transferencia2 = new Transferencia(dataFinal, BigDecimal.valueOf(200), "DEPOSITO", "De Tal", new Conta());
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);
        when(transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal)).thenReturn(transferencias);

        List<TransferenciaResponse> expectedResponses = transferencias.stream()
                .map(transferencia -> new ModelMapper().map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());

        List<TransferenciaResponse> actualResponses = transferenciaService.retornaTransferenciaByDatas(dataInicial, dataFinal);

        assertEquals(expectedResponses, actualResponses);
    }

    @Test
    public void testFindByNomeOperadorTransacao() {
        String nomeOperadorTransacao = "Fulano";
        Transferencia transferencia1 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(100), "DEPOSITO", nomeOperadorTransacao, new Conta());
        Transferencia transferencia2 = new Transferencia(LocalDateTime.now(), BigDecimal.valueOf(200), "DEPOSITO", nomeOperadorTransacao, new Conta());
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);
        when(transferenciaRepository.findByNomeOperadorTransacao(nomeOperadorTransacao)).thenReturn(transferencias);

        List<TransferenciaResponse> expectedResponses = transferencias.stream()
                .map(transferencia -> new ModelMapper().map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());

        List<TransferenciaResponse> actualResponses = transferenciaService.findByNomeOperadorTransacao(nomeOperadorTransacao);

        assertEquals(expectedResponses, actualResponses);
    }

    @Test
    public void testRetornaTransferenciaComFiltros() {
        Long contaId = 1L;
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusDays(1);
        String nomeOperadorTransacao = "Fulano";
        Transferencia transferencia1 = new Transferencia(dataInicial, BigDecimal.valueOf(100), "DEPOSITO", nomeOperadorTransacao, new Conta());
        Transferencia transferencia2 = new Transferencia(dataFinal, BigDecimal.valueOf(200), "DEPOSITO", nomeOperadorTransacao, new Conta());
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);
        when(transferenciaRepository.findAllByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(contaId, dataInicial, dataFinal, nomeOperadorTransacao)).thenReturn(transferencias);

        List<TransferenciaResponse> expectedResponses = transferencias.stream()
                .map(transferencia -> new ModelMapper().map(transferencia, TransferenciaResponse.class))
                .collect(Collectors.toList());

        List<TransferenciaResponse> actualResponses = transferenciaService.retornaTransferenciaComFiltros(contaId, dataInicial, dataFinal, nomeOperadorTransacao);

        assertEquals(expectedResponses, actualResponses);
    }
}
