package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import com.withdev.orcamentos.servico.Servico;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrcamentoTest {

    private final Cliente cliente = new Cliente("Padaria", null, null, "12345678000199");
    private final Servico site = new Servico("Site", null, new BigDecimal("2000.00"));
    private final Servico manutencao = new Servico("Manutencao", null, new BigDecimal("150.00"));

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

    @Test
    void totalSemDescontoEhSomaDosItens() {
        Orcamento orcamento = new Orcamento(cliente, null);

        orcamento.adicionarItem(site, 1);
        orcamento.adicionarItem(manutencao, 3);

        assertEquals(new BigDecimal("2450.00"), orcamento.getSubtotal());
        assertEquals(new BigDecimal("2450.00"), orcamento.getTotal());
    }

    @Test
    void descontoEhAplicadoSobreSubtotal() {
        Orcamento orcamento = new Orcamento(cliente, null);
        orcamento.adicionarItem(site, 1);

        orcamento.definirDesconto(new BigDecimal("10"));

        assertEquals(new BigDecimal("2000.00"), orcamento.getSubtotal());
        assertEquals(new BigDecimal("1800.00"), orcamento.getTotal());
    }

    @Test
    void arredondaParaDuasCasas() {
        Orcamento orcamento = new Orcamento(cliente, null);
        orcamento.adicionarItem(new Servico("Logo", null, new BigDecimal("33.33")), 1);

        orcamento.definirDesconto(new BigDecimal("15"));

        // 33.33 x 15% = 4.9995 -> 5.00 ; total = 28.33
        assertEquals(new BigDecimal("28.33"), orcamento.getTotal());
    }

    @Test
    void itemGuardaPrecoDoMomento() {
        Orcamento orcamento = new Orcamento(cliente, null);
        orcamento.adicionarItem(site, 1);

        site.setPrecoBase(new BigDecimal("9999.00"));

        assertEquals(new BigDecimal("2000.00"),
                orcamento.getItens().get(0).getPrecoUnitario());
        assertEquals(new BigDecimal("2000.00"), orcamento.getTotal());
    }
}
