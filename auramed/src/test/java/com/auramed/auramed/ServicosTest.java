package com.auramed.auramed;

import com.auramed.auramed.exception.DadosInvalidosException;
import com.auramed.auramed.exception.RecursoNaoEncontradoException;
import com.auramed.auramed.exception.RegraDeNegocioException;
import com.auramed.auramed.model.*;
import com.auramed.auramed.repository.*;
import com.auramed.auramed.service.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicosTest {
    private final PacienteRepository pacientes = mock(PacienteRepository.class);
    private final ProfissionalSaudeRepository profissionais = mock(ProfissionalSaudeRepository.class);
    private final QuartoRepository quartos = mock(QuartoRepository.class);
    private final ConsultaRepository consultas = mock(ConsultaRepository.class);
    private final InternacaoRepository internacoes = mock(InternacaoRepository.class);

    private static Paciente paciente() {
        return new Paciente("Ana", "12345678901", LocalDate.of(1990, 1, 1), "31999999999", "Rua A", "ana@exemplo.com");
    }

    private static ProfissionalSaude profissional() {
        return new ProfissionalSaude("Dr. B", "CRM123", "Clínica", "31999999999", "b@exemplo.com");
    }

    @Test
    void rejeitaCpfDuplicado() {
        when(pacientes.existsByCpf("12345678901")).thenReturn(true);
        PacienteService service = new PacienteService(pacientes, consultas, internacoes);
        assertThrows(RegraDeNegocioException.class, () -> service.cadastrar(paciente()));
        verify(pacientes, never()).save(any());
    }

    @Test
    void rejeitaCpfEEmailMalFormatados() {
        PacienteService service = new PacienteService(pacientes, consultas, internacoes);
        Paciente cpfCurto = new Paciente("Ana", "123", LocalDate.of(1990, 1, 1), "3199", "Rua A", "ana@exemplo.com");
        Paciente emailInvalido = new Paciente("Ana", "12345678901", LocalDate.of(1990, 1, 1), "3199", "Rua A", "ana");
        assertThrows(DadosInvalidosException.class, () -> service.cadastrar(cpfCurto));
        assertThrows(DadosInvalidosException.class, () -> service.cadastrar(emailInvalido));
    }

    @Test
    void naoRemovePacienteComHistorico() {
        when(pacientes.buscarComBloqueio(1L)).thenReturn(Optional.of(paciente()));
        when(consultas.existsByPacienteId(1L)).thenReturn(true);
        PacienteService service = new PacienteService(pacientes, consultas, internacoes);
        assertThrows(RegraDeNegocioException.class, () -> service.remover(1L));
        verify(pacientes, never()).delete(any());
    }

    @Test
    void consultaExigePacienteExistente() {
        when(pacientes.findById(1L)).thenReturn(Optional.empty());
        ConsultaService service = new ConsultaService(consultas, pacientes, profissionais);
        assertThrows(RecursoNaoEncontradoException.class,
            () -> service.agendar(1L, 2L, LocalDateTime.of(2026, 10, 1, 14, 0), "Retorno"));
    }

    @Test
    void naoRealizaConsultaCancelada() {
        Consulta consulta = new Consulta(paciente(), profissional(), LocalDateTime.of(2026, 10, 1, 14, 0), "Retorno");
        consulta.setStatus(StatusConsulta.CANCELADA);
        when(consultas.buscarComBloqueio(5L)).thenReturn(Optional.of(consulta));
        ConsultaService service = new ConsultaService(consultas, pacientes, profissionais);
        assertThrows(RegraDeNegocioException.class, () -> service.realizar(5L, "Obs"));
    }

    @Test
    void ultimaVagaMarcaQuartoComoOcupado() {
        Quarto quarto = new Quarto("101", 1, 2);
        when(pacientes.buscarComBloqueio(1L)).thenReturn(Optional.of(paciente()));
        when(profissionais.findById(2L)).thenReturn(Optional.of(profissional()));
        when(quartos.buscarComBloqueio(3L)).thenReturn(Optional.of(quarto));
        when(internacoes.countByQuartoIdAndDataAltaIsNull(3L)).thenReturn(1L);
        when(internacoes.save(any())).thenAnswer(i -> i.getArgument(0));

        InternacaoService service = new InternacaoService(internacoes, pacientes, profissionais, quartos);
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 1, 14, 0);
        service.internar(1L, 2L, 3L, entrada, entrada.plusDays(3), null);
        assertEquals(SituacaoQuarto.OCUPADO, quarto.getSituacao());
    }

    @Test
    void impedeSegundaInternacaoAtivaDoPaciente() {
        when(pacientes.buscarComBloqueio(1L)).thenReturn(Optional.of(paciente()));
        when(internacoes.existsByPacienteIdAndDataAltaIsNull(1L)).thenReturn(true);
        InternacaoService service = new InternacaoService(internacoes, pacientes, profissionais, quartos);
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 1, 14, 0);
        assertThrows(RegraDeNegocioException.class, () -> service.internar(1L, 2L, 3L, entrada, entrada.plusDays(1), null));
    }

    @Test
    void rejeitaAltaPrevistaAntesDaEntrada() {
        InternacaoService service = new InternacaoService(internacoes, pacientes, profissionais, quartos);
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 1, 14, 0);
        assertThrows(DadosInvalidosException.class, () -> service.internar(1L, 2L, 3L, entrada, entrada.minusDays(1), null));
    }

    @Test
    void naoReduzCapacidadeAbaixoDaOcupacao() {
        when(quartos.buscarComBloqueio(3L)).thenReturn(Optional.of(new Quarto("101", 1, 3)));
        when(internacoes.countByQuartoIdAndDataAltaIsNull(3L)).thenReturn(2L);
        QuartoService service = new QuartoService(quartos, internacoes);
        assertThrows(RegraDeNegocioException.class, () -> service.atualizar(3L, new Quarto("101", 1, 1)));
    }
}
