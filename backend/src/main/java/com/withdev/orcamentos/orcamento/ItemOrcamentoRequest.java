package com.withdev.orcamentos.orcamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemOrcamentoRequest(
        @NotNull Long servicoId,
        @Positive int quantidade
) {
}
