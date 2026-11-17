# GS2-SOA – Alerta de Furto de Celular

**FIAP – Global Solution 2026.2 · CitizenTech · Arquitetura Orientada a Serviços (SOA) e Web Services**

## Integrantes

Andrey Nagata - Rm555339
Henrique Soubhia - Rm554493
Oliver Trindade - Rm554954
Pedro Gutierre - Rm555445
William Feng - Rm555132

## Descrição da solução

API REST que serve de backend para um aplicativo móvel de segurança contra furto de celulares, pensado para o cidadão que anda a pé ou de transporte público nos grandes centros urbanos.

- **Alerta preditivo de risco:** o app envia a localização do usuário e a API calcula o risco de furto naquele lugar e horário (BAIXO, MEDIO, ALTO ou CRITICO). O cálculo usa um modelo de regressão logística baseado nas ocorrências registradas por perto.
- **Registro de ocorrências:** os cidadãos registram furtos e roubos, e esses registros alimentam o modelo de risco.
- **Guia da "hora de ouro":** quando o celular é roubado, a API gera um checklist de 7 ações urgentes (bloquear o chip, os bancos, o IMEI, registrar o B.O. etc.) e acompanha o progresso de cada uma.
- **Consulta de IMEI:** permite verificar se um celular usado tem registro de roubo.

**Tecnologias:** Java 21, Spring Boot 3.4, Spring Data JPA, H2 / PostgreSQL, Flyway, Swagger (springdoc-openapi), JUnit 5.

## Como executar

**Pré-requisito:** JDK 21 (com o Java 25 os testes de integração falham).

**Pelo IntelliJ:**
1. Abra a pasta do projeto em **File → Open**.
2. Execute a classe `PedroApplication`.

**Pelo terminal:**
```bash
mvn spring-boot:run
```

**Para usar PostgreSQL em vez do H2 (opcional):**
```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

**Para rodar os testes:**
```bash
mvn test
```

## Documentação dos endpoints

Com a aplicação rodando, acesse o Swagger em **http://localhost:8080/swagger-ui.html**.

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios` | Cadastra usuário |
| POST | `/api/dispositivos` | Cadastra celular (IMEI) |
| GET | `/api/dispositivos/imei/{imei}` | Consulta se o IMEI tem registro de roubo |
| POST | `/api/ocorrencias` | Registra uma ocorrência |
| GET | `/api/ocorrencias/proximas` | Lista as ocorrências próximas a um ponto |
| POST | `/api/alertas/avaliar` | Calcula o risco na localização informada |
| POST | `/api/incidentes` | Aciona a hora de ouro após um roubo |
| PATCH | `/api/incidentes/{id}/passos/{passoId}/concluir` | Conclui um passo do checklist |

## Evidências

Os prints da aplicação funcionando estão na pasta [`docs/evidencias`](docs/evidencias).
