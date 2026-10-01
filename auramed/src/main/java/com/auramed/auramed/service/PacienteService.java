package com.auramed.auramed.service;

import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PacienteService {
    private final PacienteRepository repository;
    public PacienteService(PacienteRepository repository) { this.repository = repository; }

    @Transactional
    public Paciente cadastrar(Paciente dados) {
        validar(dados);
        if (repository.existsByCpf(dados.getCpf())) throw new IllegalStateException("CPF já cadastrado");
        return repository.save(dados);
    }

    @Transactional
    public Paciente atualizar(Long id, Paciente dados) {
        Paciente atual = buscar(id);
        validar(dados);
        if (repository.existsByCpfAndIdNot(dados.getCpf(), id)) throw new IllegalStateException("CPF já cadastrado");
        atual.setNome(dados.getNome()); atual.setCpf(dados.getCpf());
        atual.setDataNascimento(dados.getDataNascimento()); atual.setTelefone(dados.getTelefone());
        atual.setEndereco(dados.getEndereco()); atual.setEmail(dados.getEmail());
        return atual;
    }

    @Transactional(readOnly = true)
    public Paciente buscar(Long id) {
        return repository.findById(Validacao.id(id, "Paciente")).orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Paciente> listar() { return repository.findAll(); }

    private void validar(Paciente p) {
        if (p == null) throw new IllegalArgumentException("Paciente é obrigatório");
        p.setNome(Validacao.obrigatorio(p.getNome(), "Nome"));
        String cpf = Validacao.obrigatorio(p.getCpf(), "CPF");
        if (!cpf.matches("[0-9]{11}")) throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        p.setCpf(cpf);
        if (p.getDataNascimento() == null || p.getDataNascimento().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data de nascimento inválida");
        p.setTelefone(Validacao.obrigatorio(p.getTelefone(), "Telefone"));
        p.setEndereco(Validacao.obrigatorio(p.getEndereco(), "Endereço"));
        p.setEmail(Validacao.obrigatorio(p.getEmail(), "E-mail"));
    }
}
