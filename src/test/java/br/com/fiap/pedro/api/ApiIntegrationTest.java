package br.com.fiap.pedro.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private JsonNode enviarPost(String url, String body, int statusEsperado) throws Exception {
        MvcResult r = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is(statusEsperado))
                .andReturn();
        return json.readTree(r.getResponse().getContentAsString());
    }

    @Test
    void migracoesCarregaramDadosIniciais() throws Exception {
        mvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("demo@pedro.com"));
        mvc.perform(get("/api/dispositivos/imei/490154203237518"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.possuiRegistroDeRoubo").value(false));
    }

    @Test
    void cadastroDeUsuarioValidaCamposEEmailDuplicado() throws Exception {
        enviarPost("/api/usuarios", """
                {"nome":"Carlos","email":"carlos@email.com","telefoneContato":"11977776666"}""", 201);

        JsonNode erro = enviarPost("/api/usuarios", """
                {"nome":"","email":"nao-eh-email"}""", 400);
        org.assertj.core.api.Assertions.assertThat(erro.get("campos").has("email")).isTrue();

        enviarPost("/api/usuarios", """
                {"nome":"Carlos 2","email":"CARLOS@email.com"}""", 422);
    }

    @Test
    void recursoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/usuarios/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem", containsString("99999")));
    }

    @Test
    void imeiComDigitoVerificadorErradoEhRejeitado() throws Exception {
        enviarPost("/api/dispositivos", """
                {"usuarioId":1,"marca":"Motorola","modelo":"G84","imei":"490154203237519"}""", 422);
    }

    @Test
    void avaliacaoDeRiscoNaPaulistaEhMaiorQueEmAreaSemOcorrencias() throws Exception {
        JsonNode paulista = enviarPost("/api/alertas/avaliar", """
                {"latitude":-23.5610,"longitude":-46.6565,"usuarioId":1}""", 200);

        JsonNode semDados = enviarPost("/api/alertas/avaliar", """
                {"latitude":-3.1190,"longitude":-60.0217}""", 200);

        org.assertj.core.api.Assertions.assertThat(paulista.get("ocorrenciasProximas").asInt()).isGreaterThanOrEqualTo(5);
        org.assertj.core.api.Assertions.assertThat(semDados.get("ocorrenciasProximas").asInt()).isZero();
        org.assertj.core.api.Assertions.assertThat(paulista.get("score").asDouble())
                .isGreaterThan(semDados.get("score").asDouble());

        mvc.perform(get("/api/alertas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void coordenadasInvalidasRetornam400() throws Exception {
        enviarPost("/api/alertas/avaliar", """
                {"latitude":-123,"longitude":-46.6}""", 400);
    }

    @Test
    void ocorrenciasProximasVemOrdenadasPorDistancia() throws Exception {
        mvc.perform(get("/api/ocorrencias/proximas")
                        .param("latitude", "-23.5503").param("longitude", "-46.6340").param("raioMetros", "300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].distanciaMetros").value(0));
    }

    @Test
    void fluxoCompletoDaHoraDeOuro() throws Exception {
        JsonNode usuario = enviarPost("/api/usuarios", """
                {"nome":"Beatriz","email":"bia@email.com"}""", 201);
        JsonNode disp = enviarPost("/api/dispositivos", """
                {"usuarioId":%d,"marca":"Apple","modelo":"iPhone 15","imei":"356938035643809","operadora":"Claro"}"""
                .formatted(usuario.get("id").asLong()), 201);
        long dispositivoId = disp.get("id").asLong();

        JsonNode incidente = enviarPost("/api/incidentes", """
                {"dispositivoId":%d,"tipo":"ROUBO","latitude":-23.5614,"longitude":-46.6559,"descricao":"Teste"}"""
                .formatted(dispositivoId), 201);

        org.assertj.core.api.Assertions.assertThat(incidente.get("status").asText()).isEqualTo("ABERTO");
        org.assertj.core.api.Assertions.assertThat(incidente.get("statusDispositivo").asText()).isEqualTo("ROUBADO");
        org.assertj.core.api.Assertions.assertThat(incidente.get("passos")).hasSize(7);
        org.assertj.core.api.Assertions.assertThat(incidente.get("minutosRestantesHoraDeOuro").asLong()).isBetween(59L, 60L);

        enviarPost("/api/incidentes", """
                {"dispositivoId":%d,"tipo":"FURTO","latitude":-23.56,"longitude":-46.65}""".formatted(dispositivoId), 422);

        mvc.perform(get("/api/dispositivos/imei/356938035643809"))
                .andExpect(jsonPath("$.possuiRegistroDeRoubo").value(true));

        long incidenteId = incidente.get("id").asLong();
        for (JsonNode passo : incidente.get("passos")) {
            mvc.perform(patch("/api/incidentes/{id}/passos/{passo}/concluir", incidenteId, passo.get("id").asLong()))
                    .andExpect(status().isOk());
        }

        mvc.perform(get("/api/incidentes/{id}", incidenteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDO"))
                .andExpect(jsonPath("$.progresso").value(100))
                .andExpect(jsonPath("$.statusDispositivo").value("BLOQUEADO"));

        mvc.perform(get("/api/ocorrencias/proximas")
                        .param("latitude", "-23.5614").param("longitude", "-46.6559").param("raioMetros", "50"))
                .andExpect(jsonPath("$[*].descricao", hasItem("Teste")));
    }

    @Test
    void documentacaoOpenApiEstaDisponivel() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Pedro API - CitizenTech"))
                .andExpect(jsonPath("$.paths['/api/alertas/avaliar']").exists());
    }
}
