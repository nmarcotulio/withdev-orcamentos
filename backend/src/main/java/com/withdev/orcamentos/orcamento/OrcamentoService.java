package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import com.withdev.orcamentos.cliente.ClienteService;
import com.withdev.orcamentos.servico.Servico;
import com.withdev.orcamentos.servico.ServicoService;
import com.withdev.orcamentos.shared.exception.ConflitoException;
import com.withdev.orcamentos.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrcamentoService {

    private final OrcamentoRepository repository;
    private final ClienteService clienteService;
    private final ServicoService servicoService;

    public OrcamentoService(OrcamentoRepository repository,
                            ClienteService clienteService,
                            ServicoService servicoService) {
        this.repository = repository;
        this.clienteService = clienteService;
        this.servicoService = servicoService;
    }

    public List<OrcamentoResponse> listar() {
        List<OrcamentoResponse> resposta = new ArrayList<>();
        for (Orcamento orcamento : repository.findAll()) {
            resposta.add(OrcamentoResponse.from(orcamento));
        }
        return resposta;
    }

    public OrcamentoResponse buscar(Long id) {
        return OrcamentoResponse.from(buscarEntidade(id));
    }

    public OrcamentoResponse criar(OrcamentoRequest request) {
        Cliente cliente = clienteService.buscarEntidade(request.clienteId());
        Orcamento orcamento = new Orcamento(cliente, request.observacoes());
        preencher(orcamento, request.descontoPercentual(), request.itens());
        return OrcamentoResponse.from(repository.save(orcamento));
    }

    public OrcamentoResponse atualizar(Long id, AtualizarOrcamentoRequest request) {
        Orcamento orcamento = buscarEntidade(id);
        orcamento.limparItens();
        preencher(orcamento, request.descontoPercentual(), request.itens());
        return OrcamentoResponse.from(repository.save(orcamento));
    }

    public OrcamentoResponse enviar(Long id) {
        Orcamento orcamento = buscarEntidade(id);
        orcamento.enviar();
        return OrcamentoResponse.from(repository.save(orcamento));
    }

    public void excluir(Long id) {
        Orcamento orcamento = buscarEntidade(id);
        orcamento.validarExclusao();
        repository.delete(orcamento);
    }

    private void preencher(Orcamento orcamento, BigDecimal desconto, List<ItemOrcamentoRequest> itens) {
        for (ItemOrcamentoRequest item : itens) {
            Servico servico = servicoService.buscarEntidade(item.servicoId());
            if (!servico.isAtivo()) {
                throw new ConflitoException("Servico " + servico.getId() + " esta inativo");
            }
            orcamento.adicionarItem(servico, item.quantidade());
        }
        orcamento.definirDesconto(desconto);
    }

    private Orcamento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento " + id + " nao encontrado"));
    }
}
