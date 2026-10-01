package com.auramed.auramed;

import com.auramed.auramed.model.*;
import com.auramed.auramed.repository.*;
import com.auramed.auramed.service.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegrasDeAtendimentoTest {
    @Test
    void impedeDuasConsultasAgendadasNoMesmoHorario() {
        ConsultaRepository consultas = mock(ConsultaRepository.class);
        PacienteRepository pacientes = mock(PacienteRepository.class);
        ProfissionalSaudeRepository profissionais = mock(ProfissionalSaudeRepository.class);
        LocalDateTime horario = LocalDateTime.of(2026, 10, 1, 14, 0);
        when(pacientes.findById(1L)).thenReturn(Optional.of(new Paciente("Ana", "12345678901", LocalDate.of(1990, 1, 1), "31999999999", "Rua A", "ana@exemplo.com")));
        when(profissionais.buscarComBloqueio(2L)).thenReturn(Optional.of(new ProfissionalSaude("Dr. B", "CRM123", "Clínica", "31999999999", "b@exemplo.com")));
        when(consultas.existsByProfissionalIdAndDataHoraAndStatus(2L, horario, StatusConsulta.AGENDADA)).thenReturn(true);

        ConsultaService service = new ConsultaService(consultas, pacientes, profissionais);
        assertThrows(IllegalStateException.class, () -> service.agendar(1L, 2L, horario, "Avaliação"));
        verify(consultas, never()).save(any());
    }

    @Test
    void impedeInternacaoQuandoQuartoEstaCheio() {
        InternacaoRepository internacoes = mock(InternacaoRepository.class);
        PacienteRepository pacientes = mock(PacienteRepository.class);
        ProfissionalSaudeRepository profissionais = mock(ProfissionalSaudeRepository.class);
        QuartoRepository quartos = mock(QuartoRepository.class);
        when(pacientes.buscarComBloqueio(1L)).thenReturn(Optional.of(new Paciente("Ana", "12345678901", LocalDate.of(1990, 1, 1), "31999999999", "Rua A", "ana@exemplo.com")));
        when(profissionais.findById(2L)).thenReturn(Optional.of(new ProfissionalSaude("Dr. B", "CRM123", "Clínica", "31999999999", "b@exemplo.com")));
        when(quartos.buscarComBloqueio(3L)).thenReturn(Optional.of(new Quarto("101", 1, 1)));
        when(internacoes.countByQuartoIdAndDataAltaIsNull(3L)).thenReturn(1L);

        InternacaoService service = new InternacaoService(internacoes, pacientes, profissionais, quartos);
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 1, 14, 0);
        assertThrows(IllegalStateException.class, () -> service.internar(1L, 2L, 3L, entrada, entrada.plusDays(2), null));
        verify(internacoes, never()).save(any());
    }
}
