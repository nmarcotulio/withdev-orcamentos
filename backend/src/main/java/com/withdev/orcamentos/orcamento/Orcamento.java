package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import com.withdev.orcamentos.servico.Servico;
import com.withdev.orcamentos.shared.exception.ConflitoException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    private static final int VALIDADE_PADRAO_DIAS = 15;
    private static final BigDecimal CEM = new BigDecimal("100");

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

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrcamento> itens = new ArrayList<>();

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

    public void adicionarItem(Servico servico, int quantidade) {
        exigirRascunho();
        itens.add(new ItemOrcamento(this, servico, quantidade));
        recalcular();
    }

    public void limparItens() {
        exigirRascunho();
        itens.clear();
        recalcular();
    }

    public void definirDesconto(BigDecimal descontoPercentual) {
        exigirRascunho();
        if (descontoPercentual != null) {
            descontoPercentual = descontoPercentual.setScale(2, RoundingMode.HALF_UP);
        }
        this.descontoPercentual = descontoPercentual;
        recalcular();
    }

    public void enviar() {
        if (itens.isEmpty()) {
            throw new ConflitoException("Orcamento precisa de pelo menos 1 item para ser enviado");
        }
        mudarStatus(StatusOrcamento.ENVIADO);
        this.dataEnvio = LocalDate.now();
        this.dataValidade = dataEnvio.plusDays(validadeDias);
    }

    public void validarExclusao() {
        exigirRascunho();
    }

    private void exigirRascunho() {
        if (status != StatusOrcamento.RASCUNHO) {
            throw new ConflitoException("Orcamento so pode ser alterado ou excluido em RASCUNHO (status atual: " + status + ")");
        }
    }

    private void recalcular() {
        BigDecimal soma = BigDecimal.ZERO;
        for (ItemOrcamento item : itens) {
            soma = soma.add(item.getSubtotal());
        }
        this.subtotal = soma.setScale(2, RoundingMode.HALF_UP);

        BigDecimal desconto = BigDecimal.ZERO;
        if (descontoPercentual != null) {
            desconto = subtotal.multiply(descontoPercentual)
                    .divide(CEM, 2, RoundingMode.HALF_UP);
        }
        this.total = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
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

    public List<ItemOrcamento> getItens() {
        return List.copyOf(itens);
    }
}
