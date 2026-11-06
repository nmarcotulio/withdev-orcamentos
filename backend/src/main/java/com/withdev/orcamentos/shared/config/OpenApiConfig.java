package com.withdev.orcamentos.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("WithDev Orcamentos API")
                .description("Cadastro de clientes e servicos, montagem de orcamentos com itens e desconto, "
                        + "ciclo de vida RASCUNHO > ENVIADO > APROVADO/RECUSADO/EXPIRADO.")
                .version("v1"));
    }
}
