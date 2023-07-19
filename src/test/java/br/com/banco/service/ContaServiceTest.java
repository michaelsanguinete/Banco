package br.com.banco.service;

import br.com.banco.dto.ContaResponse;
import br.com.banco.dto.SacarEDepositarRequest;
import br.com.banco.dto.TransferirRequest;
import br.com.banco.entity.Conta;
import br.com.banco.entity.Transferencia;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContaServiceTest {

    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        contaService = new ContaService(contaRepository, transferenciaRepository, modelMapper);
    }

    @Test
    public void testSacar() {
        Long contaId = 1L;
        BigDecimal saldoInicial = BigDecimal.valueOf(500);
        BigDecimal valorSaque = BigDecimal.valueOf(200);

        Conta conta = new Conta();
        conta.setId(contaId);
        conta.setSaldo(saldoInicial);
        conta.setNomeResponsavel("Fulano");

        SacarEDepositarRequest request = new SacarEDepositarRequest();
        request.setId(contaId);
        request.setValor(valorSaque);

        when(contaRepository.getById(contaId)).thenReturn(conta);

        ArgumentCaptor<Transferencia> transferenciaCaptor = ArgumentCaptor.forClass(Transferencia.class);

        contaService.sacar(request);

        assertEquals(saldoInicial.subtract(valorSaque), conta.getSaldo());
        verify(contaRepository, times(1)).save(conta);
        verify(transferenciaRepository, times(1)).save(transferenciaCaptor.capture());

        assertNotNull(transferenciaCaptor.getValue());
    }

    @Test
    public void testDepositar() {
        Long contaId = 1L;
        BigDecimal saldoInicial = BigDecimal.valueOf(500);
        BigDecimal valorDeposito = BigDecimal.valueOf(200);

        Conta conta = new Conta();
        conta.setId(contaId);
        conta.setSaldo(saldoInicial);
        conta.setNomeResponsavel("Fulano");

        SacarEDepositarRequest request = new SacarEDepositarRequest();
        request.setId(contaId);
        request.setValor(valorDeposito);

        Transferencia transferencia = new Transferencia(LocalDateTime.now(), valorDeposito, "DEPOSITO", conta.getNomeResponsavel(), conta);

        when(contaRepository.getById(contaId)).thenReturn(conta);

        ArgumentCaptor<Transferencia> transferenciaCaptor = ArgumentCaptor.forClass(Transferencia.class);

        contaService.depositar(request);

        assertAll("DEPOSITO",
                () -> assertEquals(saldoInicial.add(valorDeposito), conta.getSaldo()),
                () -> verify(contaRepository, times(1)).save(conta),
                () -> verify(transferenciaRepository, times(1)).save(transferenciaCaptor.capture())
        );

        Transferencia transferenciaCapturada = transferenciaCaptor.getValue();
        assertThat(transferenciaCapturada.getValor().compareTo(valorDeposito), equalTo(0));
        assertThat(transferenciaCapturada.getConta().getSaldo().compareTo(conta.getSaldo()), equalTo(0));
        assertThat(transferenciaCapturada.getConta().getId().compareTo((conta.getId())), equalTo(0));
        assertThat(transferenciaCapturada.getConta().getNomeResponsavel(), equalTo(conta.getNomeResponsavel()));
    }

    private boolean isEqualByComparingTo(BigDecimal valorDeposito) {
        return false;
    }

    @Test
    public void testTransferir() {
        Long contaRemetenteId = 1L;
        Long contaDestinatarioId = 2L;
        BigDecimal saldoInicialRemetente = BigDecimal.valueOf(500);
        BigDecimal saldoInicialDestinatario = BigDecimal.valueOf(200);
        BigDecimal valorTransferencia = BigDecimal.valueOf(300);

        Conta contaRemetente = new Conta();
        contaRemetente.setId(contaRemetenteId);
        contaRemetente.setSaldo(saldoInicialRemetente);
        contaRemetente.setNomeResponsavel("Fulano");

        Conta contaDestinatario = new Conta();
        contaDestinatario.setId(contaDestinatarioId);
        contaDestinatario.setSaldo(saldoInicialDestinatario);
        contaDestinatario.setNomeResponsavel("De Tal");

        TransferirRequest request = new TransferirRequest();
        request.setIdRemetente(contaRemetenteId);
        request.setIdDestinatario(contaDestinatarioId);
        request.setValor(valorTransferencia);

        Transferencia transferenciaRemetente = new Transferencia(LocalDateTime.now(), valorTransferencia.multiply(BigDecimal.valueOf(-1)), "TRANSFERENCIA", contaRemetente.getNomeResponsavel(), contaRemetente);
        Transferencia transferenciaDestinatario = new Transferencia(LocalDateTime.now(), valorTransferencia, "TRANSFERENCIA", contaDestinatario.getNomeResponsavel(), contaDestinatario);

        when(contaRepository.getById(contaRemetenteId)).thenReturn(contaRemetente);
        when(contaRepository.getById(contaDestinatarioId)).thenReturn(contaDestinatario);

        contaService.transferir(request);

        assertEquals(saldoInicialRemetente.subtract(valorTransferencia), contaRemetente.getSaldo());
        assertEquals(saldoInicialDestinatario.add(valorTransferencia), contaDestinatario.getSaldo());

        verify(contaRepository, times(2)).save(any(Conta.class));
        verify(transferenciaRepository, times(1)).save(argThat(transferencia ->
                transferencia.getValor().compareTo(transferenciaRemetente.getValor()) == 0 &&
                        transferencia.getConta().getNomeResponsavel().equals(transferenciaRemetente.getConta().getNomeResponsavel()) &&
                        transferencia.getConta().getSaldo().compareTo(transferenciaRemetente.getConta().getSaldo()) == 0 &&
                        transferencia.getTipo().equals(transferenciaRemetente.getTipo())
        ));
        verify(transferenciaRepository, times(1)).save(argThat(transferencia ->
                transferencia.getValor().compareTo(transferenciaDestinatario.getValor()) == 0 &&
                        transferencia.getConta().getNomeResponsavel().equals(transferenciaDestinatario.getConta().getNomeResponsavel()) &&
                        transferencia.getConta().getSaldo().compareTo(transferenciaDestinatario.getConta().getSaldo()) == 0 &&
                        transferencia.getTipo().equals(transferenciaDestinatario.getTipo())
        ));
    }

    @Test
    public void testRetornaContaById() {
        Long contaId = 1L;
        Conta conta = new Conta();
        conta.setId(contaId);
        conta.setNomeResponsavel("Fulano");

        when(contaRepository.getById(contaId)).thenReturn(conta);

        ContaResponse actualResponse = contaService.retornaContaById(contaId);

        assertEquals(conta.getNomeResponsavel(), actualResponse.getNomeResponsavel());
    }
}
