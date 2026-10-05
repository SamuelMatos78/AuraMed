package com.auramed.auramed.service;

import com.auramed.auramed.model.*;
import com.auramed.auramed.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class InternacaoService {
    private final InternacaoRepository repository;
    private final PacienteRepository pacientes;
    private final ProfissionalSaudeRepository profissionais;
    private final QuartoRepository quartos;
    public InternacaoService(InternacaoRepository repository, PacienteRepository pacientes,
                            ProfissionalSaudeRepository profissionais, QuartoRepository quartos) {
        this.repository = repository; this.pacientes = pacientes;
        this.profissionais = profissionais; this.quartos = quartos;
    }

    @Transactional
    public Internacao internar(Long pacienteId, Long profissionalId, Long quartoId,
                              LocalDateTime entrada, LocalDateTime previstaAlta, String observacoes) {
        if (entrada == null) throw new IllegalArgumentException("Data de entrada é obrigatória");
        if (previstaAlta == null || previstaAlta.isBefore(entrada))
            throw new IllegalArgumentException("Alta prevista anterior à entrada");
        Paciente paciente = pacientes.buscarComBloqueio(Validacao.id(pacienteId, "Paciente"))
            .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
        if (repository.existsByPacienteIdAndDataAltaIsNull(pacienteId))
            throw new IllegalStateException("Paciente já possui internação ativa");
        ProfissionalSaude profissional = profissionais.findById(Validacao.id(profissionalId, "Profissional"))
            .orElseThrow(() -> new NoSuchElementException("Profissional não encontrado"));
        Quarto quarto = quartos.buscarComBloqueio(Validacao.id(quartoId, "Quarto"))
            .orElseThrow(() -> new NoSuchElementException("Quarto não encontrado"));
        long ocupacao = repository.countByQuartoIdAndDataAltaIsNull(quartoId);
        if (ocupacao >= quarto.getCapacidadeMaxima()) throw new IllegalStateException("Quarto sem vagas");
        Internacao internacao = repository.save(new Internacao(paciente, profissional, quarto, entrada, previstaAlta, observacoes));
        quarto.setSituacao(ocupacao + 1 >= quarto.getCapacidadeMaxima() ? SituacaoQuarto.OCUPADO : SituacaoQuarto.DISPONIVEL);
        return internacao;
    }

    @Transactional
    public Internacao darAlta(Long id, LocalDateTime dataAlta, String observacoes) {
        if (dataAlta == null) throw new IllegalArgumentException("Data da alta é obrigatória");
        Internacao internacao = repository.buscarComBloqueio(Validacao.id(id, "Internação"))
            .orElseThrow(() -> new NoSuchElementException("Internação não encontrada"));
        if (internacao.getDataAlta() != null) throw new IllegalStateException("Internação já encerrada");
        if (dataAlta.isBefore(internacao.getDataEntrada())) throw new IllegalArgumentException("Alta anterior à entrada");
        Quarto quarto = quartos.buscarComBloqueio(internacao.getQuarto().getId())
            .orElseThrow(() -> new NoSuchElementException("Quarto não encontrado"));
        long ocupacao = repository.countByQuartoIdAndDataAltaIsNull(quarto.getId());
        internacao.setDataAlta(dataAlta);
        internacao.setObservacoes(observacoes);
        quarto.setSituacao(ocupacao - 1 >= quarto.getCapacidadeMaxima() ? SituacaoQuarto.OCUPADO : SituacaoQuarto.DISPONIVEL);
        return internacao;
    }

    @Transactional(readOnly = true)
    public Internacao buscar(Long id) {
        return repository.findById(Validacao.id(id, "Internação")).orElseThrow(() -> new NoSuchElementException("Internação não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Internacao> listar() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public List<InternacaoResumo> listarResumos() {
        return repository.listarComDetalhes().stream().map(InternacaoResumo::de).toList();
    }

    @Transactional(readOnly = true)
    public List<Paciente> pacientesDisponiveis() {
        Set<Long> internados = new HashSet<>(repository.idsPacientesInternados());
        return pacientes.findAll().stream().filter(p -> !internados.contains(p.getId())).toList();
    }
}
