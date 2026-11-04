package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrcamentoTest {

    private final Cliente cliente = new Cliente("Padaria", null, null, "12345678000199");

    @Test
    void orcamentoNovoComecaComoRascunhoEZerado() {
        Orcamento orcamento = new Orcamento(cliente, null);

        assertEquals(StatusOrcamento.RASCUNHO, orcamento.getStatus());
        assertEquals(BigDecimal.ZERO, orcamento.getTotal());
        assertEquals(15, orcamento.getValidadeDias());
    }

    @Test
    void transicaoProibidaLancaErro() {
        Orcamento orcamento = new Orcamento(cliente, null);

        assertThrows(TransicaoInvalidaException.class,
                () -> orcamento.mudarStatus(StatusOrcamento.APROVADO));
    }
}
