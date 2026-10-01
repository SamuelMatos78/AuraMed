package com.auramed.auramed.service;

import com.auramed.auramed.model.*;
import com.auramed.auramed.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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
        Paciente paciente = pacientes.findById(Validacao.id(pacienteId, "Paciente"))
            .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
        ProfissionalSaude profissional = profissionais.buscarComBloqueio(Validacao.id(profissionalId, "Profissional"))
            .orElseThrow(() -> new NoSuchElementException("Profissional não encontrado"));
        if (repository.existsByProfissionalIdAndDataHoraAndStatus(profissionalId, dataHora, StatusConsulta.AGENDADA))
            throw new IllegalStateException("Profissional já tem consulta agendada nesse horário");
        return repository.save(new Consulta(paciente, profissional, dataHora, motivoValidado));
    }

    @Transactional
    public Consulta realizar(Long id, String observacoesMedicas) {
        Consulta consulta = repository.buscarComBloqueio(Validacao.id(id, "Consulta"))
            .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));
        if (consulta.getStatus() != StatusConsulta.AGENDADA) throw new IllegalStateException("Consulta não está agendada");
        consulta.setObservacoesMedicas(observacoesMedicas);
        consulta.setStatus(StatusConsulta.REALIZADA);
        return consulta;
    }

    @Transactional
    public Consulta cancelar(Long id) {
        Consulta consulta = repository.buscarComBloqueio(Validacao.id(id, "Consulta"))
            .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));
        if (consulta.getStatus() != StatusConsulta.AGENDADA) throw new IllegalStateException("Consulta não está agendada");
        consulta.setStatus(StatusConsulta.CANCELADA);
        return consulta;
    }

    @Transactional(readOnly = true)
    public Consulta buscar(Long id) {
        return repository.findById(Validacao.id(id, "Consulta")).orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Consulta> listar() { return repository.findAll(); }
}
