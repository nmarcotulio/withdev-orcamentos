package com.withdev.orcamentos.orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record OrcamentoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        StatusOrcamento status,
        List<ItemOrcamentoResponse> itens,
        BigDecimal subtotal,
        BigDecimal descontoPercentual,
        BigDecimal total,
        int validadeDias,
        LocalDate dataEnvio,
        LocalDate dataValidade,
        LocalDateTime criadoEm,
        String observacoes
) {

    public static OrcamentoResponse from(Orcamento orcamento) {
        List<ItemOrcamentoResponse> itens = new ArrayList<>();
        for (ItemOrcamento item : orcamento.getItens()) {
            itens.add(ItemOrcamentoResponse.from(item));
        }
        return new OrcamentoResponse(
                orcamento.getId(),
                orcamento.getCliente().getId(),
                orcamento.getCliente().getNome(),
                orcamento.getStatus(),
                itens,
                orcamento.getSubtotal(),
                orcamento.getDescontoPercentual(),
                orcamento.getTotal(),
                orcamento.getValidadeDias(),
                orcamento.getDataEnvio(),
                orcamento.getDataValidade(),
                orcamento.getCriadoEm(),
                orcamento.getObservacoes()
        );
    }
}
