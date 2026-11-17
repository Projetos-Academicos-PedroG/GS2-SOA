# Evidências da aplicação

Coloque aqui os prints/registros da aplicação rodando. Sugestão do que capturar:

1. `swagger-ui.png` – tela do Swagger em http://localhost:8080/swagger-ui.html com todos os grupos de endpoints.
2. `alerta-paulista.png` – resposta de `POST /api/alertas/avaliar` na Paulista (nível ALTO/CRÍTICO).
3. `alerta-sem-risco.png` – mesma chamada numa área sem ocorrências (nível BAIXO).
4. `hora-de-ouro.png` – resposta de `POST /api/incidentes` com o checklist de 7 passos.
5. `incidente-concluido.png` – `GET /api/incidentes/{id}` com progresso 100% e aparelho BLOQUEADO.
6. `flyway.png` – log de inicialização mostrando as migrações V1, V2 e V3 aplicadas
   (ou a tabela `flyway_schema_history` no H2 Console: http://localhost:8080/h2-console).
7. `testes.png` – saída do `mvn test` com todos os testes passando (ou a aba Actions do GitHub verde).
