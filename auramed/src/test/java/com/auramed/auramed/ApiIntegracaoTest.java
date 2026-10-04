package com.auramed.auramed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:auramed-test;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ApiIntegracaoTest {
    @Autowired
    private MockMvc mvc;

    private long criar(String url, String json) throws Exception {
        MvcResult r = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated())
            .andReturn();
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new AssertionError("Resposta sem id: " + r.getResponse().getContentAsString());
        return Long.parseLong(m.group(1));
    }

    private long paciente(String nome, String cpf) throws Exception {
        return criar("/api/pacientes", """
            {"nome":"%s","cpf":"%s","dataNascimento":"1990-05-10","telefone":"31999990000",
             "endereco":"Rua das Flores, 10","email":"paciente@exemplo.com"}""".formatted(nome, cpf));
    }

    private long profissional() throws Exception {
        return criar("/api/profissionais", """
            {"nome":"Dra. Carla","registroProfissional":"CRM-MG 1234","especialidade":"Cardiologia",
             "telefone":"3133330000","email":"carla@exemplo.com"}""");
    }

    @Test
    void fluxoDeConsultasEHistorico() throws Exception {
        long paciente = paciente("Ana Souza", "12345678901");
        long medico = profissional();
        String consulta = """
            {"pacienteId":%d,"profissionalId":%d,"data":"2026-11-03","horario":"14:30","motivo":"Dor no peito"}"""
            .formatted(paciente, medico);

        long consultaId = criar("/api/consultas", consulta);

        mvc.perform(post("/api/consultas").contentType(MediaType.APPLICATION_JSON).content(consulta))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.mensagem").value("Profissional já tem consulta agendada nesse horário"));

        mvc.perform(get("/api/profissionais/{id}/agenda", medico).param("data", "2026-11-03"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].horario").value("14:30:00"));

        mvc.perform(patch("/api/consultas/{id}/realizar", consultaId).contentType(MediaType.APPLICATION_JSON)
                .content("{\"observacoesMedicas\":\"ECG normal\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REALIZADA"));

        mvc.perform(get("/api/pacientes/{id}/historico", paciente))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paciente.nome").value("Ana Souza"))
            .andExpect(jsonPath("$.consultas", hasSize(1)))
            .andExpect(jsonPath("$.consultas[0].observacoesMedicas").value("ECG normal"));

        mvc.perform(delete("/api/pacientes/{id}", paciente)).andExpect(status().isConflict());
    }

    @Test
    void fluxoDeInternacaoRespeitaCapacidadeDoQuarto() throws Exception {
        long ana = paciente("Ana Souza", "12345678901");
        long bruno = paciente("Bruno Lima", "98765432100");
        long medico = profissional();
        long quarto = criar("/api/quartos", "{\"numero\":\"201\",\"andar\":2,\"capacidadeMaxima\":1}");
        String internacao = """
            {"pacienteId":%d,"profissionalId":%d,"quartoId":%d,"dataEntrada":"2026-11-03T08:00",
             "dataPrevistaAlta":"2026-11-06T08:00","observacoes":"Observação cardíaca"}""";

        long internacaoId = criar("/api/internacoes", internacao.formatted(ana, medico, quarto));

        mvc.perform(get("/api/quartos/{id}", quarto))
            .andExpect(jsonPath("$.situacao").value("OCUPADO"))
            .andExpect(jsonPath("$.vagasDisponiveis").value(0));
        mvc.perform(get("/api/quartos/disponiveis")).andExpect(jsonPath("$", hasSize(0)));

        mvc.perform(post("/api/internacoes").contentType(MediaType.APPLICATION_JSON)
                .content(internacao.formatted(bruno, medico, quarto)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.mensagem").value("Quarto sem vagas"));

        mvc.perform(patch("/api/internacoes/{id}/alta", internacaoId).contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataAlta\":\"2026-11-05T10:00\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativa").value(false))
            .andExpect(jsonPath("$.observacoes").value("Observação cardíaca"));

        mvc.perform(get("/api/quartos/{id}", quarto)).andExpect(jsonPath("$.situacao").value("DISPONIVEL"));
        criar("/api/internacoes", internacao.formatted(bruno, medico, quarto));

        mvc.perform(get("/api/pacientes/{id}/historico", ana))
            .andExpect(jsonPath("$.internacoes", hasSize(1)))
            .andExpect(jsonPath("$.internacoes[0].dataAlta").value(startsWith("2026-11-05T10:00")));
        mvc.perform(get("/api/internacoes").param("ativas", "true")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void errosRetornamCorpoPadronizado() throws Exception {
        mvc.perform(get("/api/pacientes/{id}", 999))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.mensagem").value("Paciente não encontrado"))
            .andExpect(jsonPath("$.caminho").value("/api/pacientes/999"));

        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ana\",\"cpf\":\"123\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("CPF deve conter 11 dígitos"));

        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ana\",\"dataNascimento\":\"10/05/1990\"}"))
            .andExpect(status().isBadRequest());

        mvc.perform(get("/api/pacientes/abc")).andExpect(status().isBadRequest());

        mvc.perform(post("/api/pacientes").contentType(MediaType.TEXT_PLAIN).content("Ana"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.status").value(415));

        paciente("Ana Souza", "12345678901");
        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON).content("""
                {"nome":"Outra","cpf":"12345678901","dataNascimento":"1990-05-10","telefone":"3199",
                 "endereco":"Rua B","email":"outra@exemplo.com"}"""))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.mensagem").value("CPF já cadastrado"));
    }
}
