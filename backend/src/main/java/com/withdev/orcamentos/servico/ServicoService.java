package com.withdev.orcamentos.servico;

import com.withdev.orcamentos.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public List<ServicoResponse> listar() {
        List<ServicoResponse> resposta = new ArrayList<>();
        for (Servico servico : repository.findAll()) {
            resposta.add(ServicoResponse.from(servico));
        }
        return resposta;
    }

    public ServicoResponse buscar(Long id) {
        return ServicoResponse.from(buscarEntidade(id));
    }

    public ServicoResponse criar(ServicoRequest request) {
        Servico servico = new Servico(request.nome(), request.descricao(), request.precoBase());
        return ServicoResponse.from(repository.save(servico));
    }

    public ServicoResponse atualizar(Long id, ServicoRequest request) {
        Servico servico = buscarEntidade(id);
        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPrecoBase(request.precoBase());
        return ServicoResponse.from(repository.save(servico));
    }

    public void desativar(Long id) {
        Servico servico = buscarEntidade(id);
        servico.desativar();
        repository.save(servico);
    }

    public Servico buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico " + id + " nao encontrado"));
    }
}
