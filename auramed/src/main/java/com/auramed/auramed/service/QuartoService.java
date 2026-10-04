package com.auramed.auramed.service;

import com.auramed.auramed.exception.*;
import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.model.SituacaoQuarto;
import com.auramed.auramed.repository.InternacaoRepository;
import com.auramed.auramed.repository.QuartoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuartoService {
    private final QuartoRepository repository;
    private final InternacaoRepository internacoes;
    public QuartoService(QuartoRepository repository, InternacaoRepository internacoes) {
        this.repository = repository; this.internacoes = internacoes;
    }

    @Transactional
    public Quarto cadastrar(Quarto dados) {
        validar(dados);
        if (repository.existsByNumero(dados.getNumero())) throw new RegraDeNegocioException("Número de quarto já cadastrado");
        dados.setSituacao(SituacaoQuarto.DISPONIVEL);
        return repository.save(dados);
    }

    @Transactional
    public Quarto atualizar(Long id, Quarto dados) {
        Quarto atual = repository.buscarComBloqueio(Validacao.id(id, "Quarto"))
            .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado"));
        validar(dados);
        if (repository.existsByNumeroAndIdNot(dados.getNumero(), id)) throw new RegraDeNegocioException("Número de quarto já cadastrado");
        long ocupacao = internacoes.countByQuartoIdAndDataAltaIsNull(id);
        if (dados.getCapacidadeMaxima() < ocupacao) throw new RegraDeNegocioException("Capacidade inferior à ocupação atual");
        atual.setNumero(dados.getNumero()); atual.setAndar(dados.getAndar());
        atual.setCapacidadeMaxima(dados.getCapacidadeMaxima());
        atual.setSituacao(ocupacao >= dados.getCapacidadeMaxima() ? SituacaoQuarto.OCUPADO : SituacaoQuarto.DISPONIVEL);
        return atual;
    }

    @Transactional
    public void remover(Long id) {
        Quarto quarto = repository.buscarComBloqueio(Validacao.id(id, "Quarto"))
            .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado"));
        if (internacoes.existsByQuartoId(id))
            throw new RegraDeNegocioException("Quarto possui internações registradas e não pode ser removido");
        repository.delete(quarto);
    }

    @Transactional(readOnly = true)
    public List<Quarto> listarDisponiveis() {
        return repository.findBySituacaoOrderByAndarAscNumeroAsc(SituacaoQuarto.DISPONIVEL);
    }

    @Transactional(readOnly = true)
    public long ocupacao(Long id) {
        return internacoes.countByQuartoIdAndDataAltaIsNull(Validacao.id(id, "Quarto"));
    }

    @Transactional(readOnly = true)
    public Quarto buscar(Long id) {
        return repository.findById(Validacao.id(id, "Quarto")).orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Quarto> listar() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public long vagasDisponiveis(Long id) {
        Quarto quarto = buscar(id);
        return quarto.getCapacidadeMaxima() - internacoes.countByQuartoIdAndDataAltaIsNull(id);
    }

    private void validar(Quarto q) {
        if (q == null) throw new DadosInvalidosException("Quarto é obrigatório");
        q.setNumero(Validacao.obrigatorio(q.getNumero(), "Número do quarto"));
        if (q.getAndar() == null) throw new DadosInvalidosException("Andar é obrigatório");
        if (q.getCapacidadeMaxima() == null || q.getCapacidadeMaxima() < 1)
            throw new DadosInvalidosException("Capacidade máxima deve ser positiva");
    }
}
