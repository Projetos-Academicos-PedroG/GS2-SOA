package br.com.fiap.pedro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI pedroOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedro API - CitizenTech")
                        .version("1.0.0")
                        .description("""
                                Backend do aplicativo de **segurança preventiva contra furto de celulares**.

                                * Alertas preditivos de risco por geolocalização (modelo de Machine Learning - regressão logística)
                                * Base colaborativa de ocorrências
                                * Guia automatizado de resposta rápida na "hora de ouro" após o furto
                                """)
                        .contact(new Contact().name("Equipe Pedro - FIAP Global Solution 2026.2"))
                        .license(new License().name("MIT")))
                .tags(List.of(
                        new Tag().name("Usuários").description("Cadastro de cidadãos"),
                        new Tag().name("Dispositivos").description("Celulares cadastrados (IMEI)"),
                        new Tag().name("Ocorrências").description("Registros de furto/roubo usados pelo modelo"),
                        new Tag().name("Alertas de risco").description("Avaliação preditiva de risco por localização"),
                        new Tag().name("Hora de ouro").description("Incidentes e checklist de resposta rápida")));
    }
}
