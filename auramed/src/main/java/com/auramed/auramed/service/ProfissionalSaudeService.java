package com.auramed.auramed.service;

import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.repository.ProfissionalSaudeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProfissionalSaudeService {
    private final ProfissionalSaudeRepository repository;
    public ProfissionalSaudeService(ProfissionalSaudeRepository repository) { this.repository = repository; }

    @Transactional
    public ProfissionalSaude cadastrar(ProfissionalSaude dados) {
        validar(dados);
        if (repository.existsByRegistroProfissional(dados.getRegistroProfissional()))
            throw new IllegalStateException("Registro profissional já cadastrado");
        return repository.save(dados);
    }

    @Transactional
    public ProfissionalSaude atualizar(Long id, ProfissionalSaude dados) {
        ProfissionalSaude atual = buscar(id);
        validar(dados);
        if (repository.existsByRegistroProfissionalAndIdNot(dados.getRegistroProfissional(), id))
            throw new IllegalStateException("Registro profissional já cadastrado");
        atual.setNome(dados.getNome()); atual.setRegistroProfissional(dados.getRegistroProfissional());
        atual.setEspecialidade(dados.getEspecialidade()); atual.setTelefone(dados.getTelefone());
        atual.setEmail(dados.getEmail());
        return atual;
    }

    @Transactional(readOnly = true)
    public ProfissionalSaude buscar(Long id) {
        return repository.findById(Validacao.id(id, "Profissional")).orElseThrow(() -> new NoSuchElementException("Profissional não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ProfissionalSaude> listar() { return repository.findAll(); }

    private void validar(ProfissionalSaude p) {
        if (p == null) throw new IllegalArgumentException("Profissional é obrigatório");
        p.setNome(Validacao.obrigatorio(p.getNome(), "Nome"));
        p.setRegistroProfissional(Validacao.obrigatorio(p.getRegistroProfissional(), "Registro profissional"));
        p.setEspecialidade(Validacao.obrigatorio(p.getEspecialidade(), "Especialidade"));
        p.setTelefone(Validacao.obrigatorio(p.getTelefone(), "Telefone"));
        p.setEmail(Validacao.obrigatorio(p.getEmail(), "E-mail"));
    }
}
