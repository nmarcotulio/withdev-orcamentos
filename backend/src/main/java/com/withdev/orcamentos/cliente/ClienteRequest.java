package com.withdev.orcamentos.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 120) String nome,
        @Email @Size(max = 120) String email,
        @Size(max = 20) String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "deve ter 11 (CPF) ou 14 (CNPJ) digitos, so numeros")
        String documento
) {
}
