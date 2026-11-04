package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    private static final int VALIDADE_PADRAO_DIAS = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrcamento status;

    @Column(precision = 5, scale = 2)
    private BigDecimal descontoPercentual;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private int validadeDias;

    private LocalDate dataEnvio;

    private LocalDate dataValidade;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(length = 1000)
    private String observacoes;

    protected Orcamento() {
    }

    public Orcamento(Cliente cliente, String observacoes) {
        this.cliente = cliente;
        this.observacoes = observacoes;
        this.status = StatusOrcamento.RASCUNHO;
        this.validadeDias = VALIDADE_PADRAO_DIAS;
        this.subtotal = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    void mudarStatus(StatusOrcamento novo) {
        if (!status.podeTransicionarPara(novo)) {
            throw new TransicaoInvalidaException(status, novo);
        }
        this.status = novo;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusOrcamento getStatus() {
        return status;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public int getValidadeDias() {
        return validadeDias;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getObservacoes() {
        return observacoes;
    }
}
