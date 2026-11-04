package com.withdev.orcamentos.orcamento;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusOrcamentoTest {

    @Test
    void rascunhoSoPodeIrParaEnviado() {
        assertTrue(StatusOrcamento.RASCUNHO.podeTransicionarPara(StatusOrcamento.ENVIADO));
        assertFalse(StatusOrcamento.RASCUNHO.podeTransicionarPara(StatusOrcamento.APROVADO));
    }

    @Test
    void enviadoPodeSerAprovadoRecusadoOuExpirado() {
        assertTrue(StatusOrcamento.ENVIADO.podeTransicionarPara(StatusOrcamento.APROVADO));
        assertTrue(StatusOrcamento.ENVIADO.podeTransicionarPara(StatusOrcamento.RECUSADO));
        assertTrue(StatusOrcamento.ENVIADO.podeTransicionarPara(StatusOrcamento.EXPIRADO));
        assertFalse(StatusOrcamento.ENVIADO.podeTransicionarPara(StatusOrcamento.RASCUNHO));
    }

    @Test
    void aprovadoNaoMudaMais() {
        assertFalse(StatusOrcamento.APROVADO.podeTransicionarPara(StatusOrcamento.ENVIADO));
        assertFalse(StatusOrcamento.APROVADO.podeTransicionarPara(StatusOrcamento.RECUSADO));
    }
}
