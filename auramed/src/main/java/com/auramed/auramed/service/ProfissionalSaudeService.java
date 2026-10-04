package com.auramed.auramed.service;

import com.auramed.auramed.exception.*;
import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.repository.ConsultaRepository;
import com.auramed.auramed.repository.InternacaoRepository;
import com.auramed.auramed.repository.ProfissionalSaudeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProfissionalSaudeService {
    private final ProfissionalSaudeRepository repository;
    private final ConsultaRepository consultas;
    private final InternacaoRepository internacoes;
    public ProfissionalSaudeService(ProfissionalSaudeRepository repository, ConsultaRepository consultas, InternacaoRepository internacoes) {
        this.repository = repository; this.consultas = consultas; this.internacoes = internacoes;
    }

    @Transactional
    public ProfissionalSaude cadastrar(ProfissionalSaude dados) {
        validar(dados);
        if (repository.existsByRegistroProfissional(dados.getRegistroProfissional()))
            throw new RegraDeNegocioException("Registro profissional já cadastrado");
        return repository.save(dados);
    }

    @Transactional
    public ProfissionalSaude atualizar(Long id, ProfissionalSaude dados) {
        ProfissionalSaude atual = buscar(id);
        validar(dados);
        if (repository.existsByRegistroProfissionalAndIdNot(dados.getRegistroProfissional(), id))
            throw new RegraDeNegocioException("Registro profissional já cadastrado");
        atual.setNome(dados.getNome()); atual.setRegistroProfissional(dados.getRegistroProfissional());
        atual.setEspecialidade(dados.getEspecialidade()); atual.setTelefone(dados.getTelefone());
        atual.setEmail(dados.getEmail());
        return atual;
    }

    @Transactional
    public void remover(Long id) {
        ProfissionalSaude profissional = repository.buscarComBloqueio(Validacao.id(id, "Profissional"))
            .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        if (consultas.existsByProfissionalId(id) || internacoes.existsByProfissionalId(id))
            throw new RegraDeNegocioException("Profissional possui atendimentos registrados e não pode ser removido");
        repository.delete(profissional);
    }

    @Transactional(readOnly = true)
    public List<Consulta> agenda(Long id, LocalDate data) {
        buscar(id);
        if (data == null) throw new DadosInvalidosException("Data é obrigatória");
        return consultas.findByProfissionalIdAndDataHoraBetweenOrderByDataHora(id, data.atStartOfDay(), data.plusDays(1).atStartOfDay().minusNanos(1));
    }

    @Transactional(readOnly = true)
    public ProfissionalSaude buscar(Long id) {
        return repository.findById(Validacao.id(id, "Profissional")).orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ProfissionalSaude> listar() { return repository.findAll(); }

    private void validar(ProfissionalSaude p) {
        if (p == null) throw new DadosInvalidosException("Profissional é obrigatório");
        p.setNome(Validacao.obrigatorio(p.getNome(), "Nome"));
        p.setRegistroProfissional(Validacao.obrigatorio(p.getRegistroProfissional(), "Registro profissional"));
        p.setEspecialidade(Validacao.obrigatorio(p.getEspecialidade(), "Especialidade"));
        p.setTelefone(Validacao.obrigatorio(p.getTelefone(), "Telefone"));
        p.setEmail(Validacao.email(p.getEmail()));
    }
}
