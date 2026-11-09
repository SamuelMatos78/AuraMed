package com.auramed.auramed;

import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.service.ConsultaService;
import com.auramed.auramed.service.PacienteService;
import com.auramed.auramed.service.ProfissionalSaudeService;
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
    "spring.datasource.url=jdbc:h2:mem:auramed-consultas;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaginaConsultasTest {
    @Autowired private MockMvc mvc;
    @Autowired private PacienteService pacientes;
    @Autowired private ProfissionalSaudeService profissionais;
    @Autowired private ConsultaService consultas;

    @Test
    void semConsultasMostraEstadoVazio() throws Exception {
        mvc.perform(get("/consultas").accept("text/html"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Nenhuma consulta agendada")));
    }

    @Test
    void listaConsultasDoBancoEPopulaFormulario() throws Exception {
        Paciente ana = pacientes.cadastrar(new Paciente("Ana Souza", "12345678901", LocalDate.of(1990, 5, 10), "31999990000", "Rua A, 1", "ana@exemplo.com"));
        ProfissionalSaude medica = profissionais.cadastrar(new ProfissionalSaude("Dra. Carla Reis", "CRM-MG 1234", "Cardiologia", "3133330000", "carla@exemplo.com"));
        consultas.agendar(ana.getId(), medica.getId(), LocalDateTime.now().plusDays(3).withHour(9).withMinute(0).withSecond(0).withNano(0), "Avaliação cardiológica");

        mvc.perform(get("/consultas").accept("text/html"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Avaliação cardiológica")))
            .andExpect(content().string(containsString("consulta-status--agendada")))
            .andExpect(content().string(containsString("Dra. Carla Reis — Cardiologia")))
            .andExpect(content().string(containsString("1 consulta")))
            .andExpect(content().string(containsString("btn-cancelar")));
    }
}
