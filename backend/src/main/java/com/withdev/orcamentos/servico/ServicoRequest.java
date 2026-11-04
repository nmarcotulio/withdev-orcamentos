package com.withdev.orcamentos.servico;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 500) String descricao,
        @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) BigDecimal precoBase
) {
}
