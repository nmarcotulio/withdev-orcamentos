package com.withdev.orcamentos.cliente;

import com.withdev.orcamentos.shared.exception.ConflitoException;
import com.withdev.orcamentos.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteResponse> listar() {
        List<ClienteResponse> resposta = new ArrayList<>();
        for (Cliente cliente : repository.findAll()) {
            resposta.add(ClienteResponse.from(cliente));
        }
        return resposta;
    }

    public ClienteResponse buscar(Long id) {
        return ClienteResponse.from(buscarEntidade(id));
    }

    public ClienteResponse criar(ClienteRequest request) {
        if (repository.existsByDocumento(request.documento())) {
            throw new ConflitoException("Ja existe cliente com o documento " + request.documento());
        }
        Cliente cliente = new Cliente(request.nome(), request.email(), request.telefone(), request.documento());
        return ClienteResponse.from(repository.save(cliente));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidade(id);
        boolean trocouDocumento = !cliente.getDocumento().equals(request.documento());
        if (trocouDocumento && repository.existsByDocumento(request.documento())) {
            throw new ConflitoException("Ja existe cliente com o documento " + request.documento());
        }
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setDocumento(request.documento());
        return ClienteResponse.from(repository.save(cliente));
    }

    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    public Cliente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " nao encontrado"));
    }
}
