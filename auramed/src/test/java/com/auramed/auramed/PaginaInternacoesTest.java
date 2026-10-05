package com.auramed.auramed;

import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.service.InternacaoService;
import com.auramed.auramed.service.PacienteService;
import com.auramed.auramed.service.ProfissionalSaudeService;
import com.auramed.auramed.service.QuartoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:auramed-pagina;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaginaInternacoesTest {
    @Autowired private MockMvc mvc;
    @Autowired private PacienteService pacientes;
    @Autowired private ProfissionalSaudeService profissionais;
    @Autowired private QuartoService quartos;
    @Autowired private InternacaoService internacoes;

    @Test
    void semDadosMostraEstadoVazioEPendencias() throws Exception {
        mvc.perform(get("/internacoes"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Nenhuma internação registrada")))
            .andExpect(content().string(containsString("Não há profissionais da saúde cadastrados.")))
            .andExpect(content().string(not(containsString("id=\"formulario-internacao\""))));
    }

    @Test
    void listaInternacoesEOfereceSoPacientesEQuartosDisponiveis() throws Exception {
        Paciente ana = pacientes.cadastrar(new Paciente("Ana Souza", "12345678901", LocalDate.of(1990, 5, 10), "31999990000", "Rua A, 1", "ana@exemplo.com"));
        Paciente bruno = pacientes.cadastrar(new Paciente("Bruno Lima", "98765432100", LocalDate.of(1985, 3, 2), "31988880000", "Rua B, 2", "bruno@exemplo.com"));
        ProfissionalSaude medica = profissionais.cadastrar(new ProfissionalSaude("Dra. Carla Reis", "CRM-MG 1234", "Cardiologia", "3133330000", "carla@exemplo.com"));
        Quarto cheio = quartos.cadastrar(new Quarto("101", 1, 1));
        quartos.cadastrar(new Quarto("202", 2, 2));
        LocalDateTime entrada = LocalDateTime.now().minusDays(5);
        internacoes.internar(ana.getId(), medica.getId(), cheio.getId(), entrada, entrada.plusDays(2), "Observação cardíaca");

        mvc.perform(get("/internacoes"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("altasAtrasadas", 1L))
            .andExpect(model().attribute("leitosLivres", 2L))
            .andExpect(content().string(containsString("Alta atrasada")))
            .andExpect(content().string(containsString("Observação cardíaca")))
            .andExpect(content().string(containsString("id=\"formulario-internacao\"")))
            .andExpect(content().string(containsString("Bruno Lima · CPF 98765432100")))
            .andExpect(content().string(not(containsString("Ana Souza · CPF"))))
            .andExpect(content().string(containsString("Quarto 202 · 2º andar · 2 vagas")))
            .andExpect(content().string(not(containsString("Quarto 101 · 1º andar"))))
            .andExpect(content().string(containsString("data-internacao=\"1\"")));
    }
}
