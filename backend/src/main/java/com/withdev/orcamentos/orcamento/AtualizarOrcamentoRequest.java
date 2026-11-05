package com.withdev.orcamentos.orcamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AtualizarOrcamentoRequest(
        @DecimalMin("0") @DecimalMax("100") BigDecimal descontoPercentual,
        @NotNull @Valid List<ItemOrcamentoRequest> itens
) {
}
