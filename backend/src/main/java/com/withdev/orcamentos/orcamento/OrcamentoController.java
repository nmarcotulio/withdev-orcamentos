package com.withdev.orcamentos.orcamento;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrcamentoResponse> listar(@RequestParam(required = false) StatusOrcamento status,
                                          @RequestParam(required = false) Long clienteId) {
        return service.listar(status, clienteId);
    }

    @GetMapping("/{id}")
    public OrcamentoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@RequestBody @Valid OrcamentoRequest request) {
        OrcamentoResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/orcamentos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public OrcamentoResponse atualizar(@PathVariable Long id,
                                       @RequestBody @Valid AtualizarOrcamentoRequest request) {
        return service.atualizar(id, request);
    }

    @PostMapping("/{id}/enviar")
    public OrcamentoResponse enviar(@PathVariable Long id) {
        return service.enviar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
