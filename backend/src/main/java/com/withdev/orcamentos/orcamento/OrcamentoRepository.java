package com.withdev.orcamentos.orcamento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findByStatus(StatusOrcamento status);

    List<Orcamento> findByClienteId(Long clienteId);

    List<Orcamento> findByStatusAndClienteId(StatusOrcamento status, Long clienteId);

    List<Orcamento> findByStatusAndDataValidadeBefore(StatusOrcamento status, LocalDate data);
}
