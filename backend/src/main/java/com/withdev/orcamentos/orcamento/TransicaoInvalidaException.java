package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.shared.exception.ConflitoException;

public class TransicaoInvalidaException extends ConflitoException {

    public TransicaoInvalidaException(StatusOrcamento atual, StatusOrcamento novo) {
        super("Nao e possivel mudar o orcamento de " + atual + " para " + novo);
    }
}
