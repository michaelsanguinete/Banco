package br.com.banco.repository;

import br.com.banco.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByContaId(Long contaId);

    @Query("SELECT t FROM Transferencia t WHERE t.dataTransferencia BETWEEN :dataInicial AND :dataFinal")
    List<Transferencia> findByDataTransferenciaBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<Transferencia> findByNomeOperadorTransacao(String nomeOperadorTransacao);

    List<Transferencia> findAllByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(Long contaId, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperadorTransacao);
}
