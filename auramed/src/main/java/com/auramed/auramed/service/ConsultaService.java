package com.auramed.auramed.service;

import com.auramed.auramed.model.*;
import com.auramed.auramed.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class ConsultaService {
    private final ConsultaRepository repository;
    private final PacienteRepository pacientes;
    private final ProfissionalSaudeRepository profissionais;
    public ConsultaService(ConsultaRepository repository, PacienteRepository pacientes, ProfissionalSaudeRepository profissionais) {
        this.repository = repository; this.pacientes = pacientes; this.profissionais = profissionais;
    }

    @Transactional
    public Consulta agendar(Long pacienteId, Long profissionalId, LocalDateTime dataHora, String motivo) {
        if (dataHora == null) throw new IllegalArgumentException("Data e horário são obrigatórios");
        String motivoValidado = Validacao.obrigatorio(motivo, "Motivo");
        if (motivoValidado.length() > 500) throw new IllegalArgumentException("Motivo deve ter no máximo 500 caracteres");
        Long idPaciente = Validacao.id(pacienteId, "Paciente");
        Long idProfissional = Validacao.id(profissionalId, "Profissional");
        Paciente paciente = pacientes.findById(idPaciente)
            .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
        ProfissionalSaude profissional = profissionais.buscarComBloqueio(idProfissional)
            .orElseThrow(() -> new NoSuchElementException("Profissional não encontrado"));
        boolean horarioOcupado = repository.existsByProfissionalIdAndDataHoraAndStatus(
                idProfissional, dataHora, StatusConsulta.AGENDADA)
            || repository.existsByProfissionalIdAndDataHoraAndStatusIn(idProfissional, dataHora,
                Set.of(StatusConsulta.CONFIRMADA, StatusConsulta.EM_ATENDIMENTO, StatusConsulta.CONCLUIDA,
                    StatusConsulta.REALIZADA));
        if (horarioOcupado)
            throw new IllegalStateException("Profissional já tem consulta agendada nesse horário");
        return repository.save(new Consulta(paciente, profissional, dataHora, motivoValidado));
    }

    @Transactional
    public Consulta alterarStatus(Long id, StatusConsulta novoStatus, String observacoesMedicas) {
        Consulta consulta = repository.buscarComBloqueio(Validacao.id(id, "Consulta"))
            .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));
        if (novoStatus == null) throw new IllegalArgumentException("Status da consulta é obrigatório");
        novoStatus = novoStatus.normalizado();
        if (novoStatus == StatusConsulta.NAO_COMPARECEU && consulta.getDataHora().isAfter(LocalDateTime.now()))
            throw new IllegalStateException("A consulta só pode ser marcada como não compareceu após o horário agendado");
        if (observacoesMedicas != null) {
            if (novoStatus != StatusConsulta.EM_ATENDIMENTO && novoStatus != StatusConsulta.CONCLUIDA) {
                throw new IllegalArgumentException("Observações só podem ser registradas durante ou ao concluir o atendimento");
            }
        }
        consulta.alterarStatus(novoStatus);
        if (observacoesMedicas != null) consulta.registrarObservacoes(observacoesMedicas.trim());
        return consulta;
    }

    @Deprecated(forRemoval = false)
    @Transactional
    public Consulta realizar(Long id, String observacoesMedicas) {
        return alterarStatus(id, StatusConsulta.CONCLUIDA, observacoesMedicas);
    }

    @Transactional
    public Consulta cancelar(Long id) {
        return alterarStatus(id, StatusConsulta.CANCELADA, null);
    }

    @Transactional(readOnly = true)
    public Consulta buscar(Long id) {
        return repository.findById(Validacao.id(id, "Consulta")).orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Consulta> listar() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public List<Consulta> listarPorPaciente(Long pacienteId) {
        Long idValidado = Validacao.id(pacienteId, "Paciente");
        if (!pacientes.existsById(idValidado)) throw new NoSuchElementException("Paciente não encontrado");
        return repository.findByPacienteIdOrderByDataHoraDesc(idValidado);
    }
}
