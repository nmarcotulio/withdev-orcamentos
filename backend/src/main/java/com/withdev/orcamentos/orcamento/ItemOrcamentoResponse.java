package com.withdev.orcamentos.orcamento;

import java.math.BigDecimal;

public record ItemOrcamentoResponse(
        Long servicoId,
        String servicoNome,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {

    public static ItemOrcamentoResponse from(ItemOrcamento item) {
        return new ItemOrcamentoResponse(
                item.getServico().getId(),
                item.getServico().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
