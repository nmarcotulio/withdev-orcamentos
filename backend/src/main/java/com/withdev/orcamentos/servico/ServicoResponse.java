package com.withdev.orcamentos.servico;

import java.math.BigDecimal;

public record ServicoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoBase,
        boolean ativo
) {

    public static ServicoResponse from(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPrecoBase(),
                servico.isAtivo()
        );
    }
}
