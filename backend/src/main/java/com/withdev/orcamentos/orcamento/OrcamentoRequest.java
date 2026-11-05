package com.withdev.orcamentos.orcamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoRequest(
        @NotNull Long clienteId,
        @Size(max = 1000) String observacoes,
        @DecimalMin("0") @DecimalMax("100") BigDecimal descontoPercentual,
        @NotNull @Valid List<ItemOrcamentoRequest> itens
) {
}
