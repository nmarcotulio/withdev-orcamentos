package com.withdev.orcamentos.orcamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpiracaoScheduler {

    private static final Logger log = LoggerFactory.getLogger(ExpiracaoScheduler.class);

    private final OrcamentoService service;

    public ExpiracaoScheduler(OrcamentoService service) {
        this.service = service;
    }

    @Scheduled(cron = "${orcamentos.expiracao.cron:0 0 1 * * *}")
    public void expirarOrcamentosVencidos() {
        int quantidade = service.expirarVencidos(LocalDate.now());
        log.info("Expiracao diaria: {} orcamento(s) expirado(s)", quantidade);
    }
}
