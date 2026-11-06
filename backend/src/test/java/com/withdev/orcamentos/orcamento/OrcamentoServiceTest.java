package com.withdev.orcamentos.orcamento;

import com.withdev.orcamentos.cliente.Cliente;
import com.withdev.orcamentos.cliente.ClienteService;
import com.withdev.orcamentos.servico.Servico;
import com.withdev.orcamentos.servico.ServicoService;
import com.withdev.orcamentos.shared.exception.ConflitoException;
import com.withdev.orcamentos.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository repository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ServicoService servicoService;

    @InjectMocks
    private OrcamentoService service;

    private final Cliente cliente = new Cliente("Padaria", null, null, "12345678000199");
    private final Servico site = new Servico("Site", null, new BigDecimal("2000.00"));

    @Test
    void criarComServicoInativoLancaConflitoENaoSalva() {
        site.desativar();
        when(clienteService.buscarEntidade(1L)).thenReturn(cliente);
        when(servicoService.buscarEntidade(2L)).thenReturn(site);
        OrcamentoRequest request = new OrcamentoRequest(1L, null, null,
                List.of(new ItemOrcamentoRequest(2L, 1)));

        assertThrows(ConflitoException.class, () -> service.criar(request));

        verify(repository, never()).save(any());
    }

    @Test
    void criarCalculaTotalNoServidorESalva() {
        when(clienteService.buscarEntidade(1L)).thenReturn(cliente);
        when(servicoService.buscarEntidade(2L)).thenReturn(site);
        when(repository.save(any(Orcamento.class))).thenAnswer(chamada -> chamada.getArgument(0));
        OrcamentoRequest request = new OrcamentoRequest(1L, null, new BigDecimal("10"),
                List.of(new ItemOrcamentoRequest(2L, 2)));

        OrcamentoResponse resposta = service.criar(request);

        assertEquals(new BigDecimal("4000.00"), resposta.subtotal());
        assertEquals(new BigDecimal("3600.00"), resposta.total());
        assertEquals(StatusOrcamento.RASCUNHO, resposta.status());
        verify(repository).save(any(Orcamento.class));
    }

    @Test
    void buscarInexistenteLancaNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(99L));
    }

    @Test
    void expirarVencidosExpiraTodosQueORepositoryDevolveu() {
        LocalDate hoje = LocalDate.of(2026, 12, 1);
        Orcamento primeiro = orcamentoEnviado();
        Orcamento segundo = orcamentoEnviado();
        List<Orcamento> vencidos = List.of(primeiro, segundo);
        when(repository.findByStatusAndDataValidadeBefore(StatusOrcamento.ENVIADO, hoje))
                .thenReturn(vencidos);

        int quantidade = service.expirarVencidos(hoje);

        assertEquals(2, quantidade);
        assertEquals(StatusOrcamento.EXPIRADO, primeiro.getStatus());
        assertEquals(StatusOrcamento.EXPIRADO, segundo.getStatus());
        verify(repository).saveAll(vencidos);
    }

    @Test
    void expirarSemVencidosDevolveZero() {
        LocalDate hoje = LocalDate.of(2026, 12, 1);
        when(repository.findByStatusAndDataValidadeBefore(StatusOrcamento.ENVIADO, hoje))
                .thenReturn(List.of());

        assertEquals(0, service.expirarVencidos(hoje));
    }

    private Orcamento orcamentoEnviado() {
        Orcamento orcamento = new Orcamento(cliente, null);
        orcamento.adicionarItem(site, 1);
        orcamento.enviar();
        return orcamento;
    }
}
