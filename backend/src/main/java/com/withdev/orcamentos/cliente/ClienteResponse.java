package com.withdev.orcamentos.cliente;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String documento,
        LocalDateTime criadoEm
) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDocumento(),
                cliente.getCriadoEm()
        );
    }
}
