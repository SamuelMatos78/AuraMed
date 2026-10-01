package com.auramed.auramed.service;

import com.auramed.auramed.model.HistoricoMedico;
import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.model.StatusConsulta;
import com.auramed.auramed.repository.ConsultaRepository;
import com.auramed.auramed.repository.InternacaoRepository;
import com.auramed.auramed.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class HistoricoMedicoService {
    private final PacienteRepository pacientes;
    private final ConsultaRepository consultas;
    private final InternacaoRepository internacoes;
    public HistoricoMedicoService(PacienteRepository pacientes, ConsultaRepository consultas, InternacaoRepository internacoes) {
        this.pacientes = pacientes; this.consultas = consultas; this.internacoes = internacoes;
    }

    @Transactional(readOnly = true)
    public HistoricoMedico consultar(Long pacienteId) {
        Paciente paciente = pacientes.findById(Validacao.id(pacienteId, "Paciente"))
            .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
        return new HistoricoMedico(paciente,
            consultas.findByPacienteIdAndStatusOrderByDataHoraDesc(pacienteId, StatusConsulta.REALIZADA),
            internacoes.findByPacienteIdOrderByDataEntradaDesc(pacienteId));
    }
}
