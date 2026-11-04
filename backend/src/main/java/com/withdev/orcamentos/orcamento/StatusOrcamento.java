package com.withdev.orcamentos.orcamento;

public enum StatusOrcamento {
    RASCUNHO,
    ENVIADO,
    APROVADO,
    RECUSADO,
    EXPIRADO;

    public boolean podeTransicionarPara(StatusOrcamento novo) {
        return switch (this) {
            case RASCUNHO -> novo == ENVIADO;
            case ENVIADO -> novo == APROVADO || novo == RECUSADO || novo == EXPIRADO;
            case APROVADO, RECUSADO, EXPIRADO -> false;
        };
    }
}
