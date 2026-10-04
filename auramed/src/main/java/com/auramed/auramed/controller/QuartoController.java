package com.auramed.auramed.controller;

import com.auramed.auramed.dto.QuartoRequest;
import com.auramed.auramed.dto.QuartoResponse;
import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.service.QuartoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {
    private final QuartoService service;
    public QuartoController(QuartoService service) { this.service = service; }

    @GetMapping
    public List<QuartoResponse> listar() { return service.listar().stream().map(this::resposta).toList(); }

    @GetMapping("/disponiveis")
    public List<QuartoResponse> listarDisponiveis() {
        return service.listarDisponiveis().stream().map(this::resposta).toList();
    }

    @GetMapping("/{id}")
    public QuartoResponse buscar(@PathVariable Long id) { return resposta(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<QuartoResponse> cadastrar(@RequestBody QuartoRequest dados) {
        QuartoResponse criado = resposta(service.cadastrar(dados.paraEntidade()));
        return ResponseEntity.created(URI.create("/api/quartos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public QuartoResponse atualizar(@PathVariable Long id, @RequestBody QuartoRequest dados) {
        return resposta(service.atualizar(id, dados.paraEntidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    private QuartoResponse resposta(Quarto quarto) { return QuartoResponse.de(quarto, service.ocupacao(quarto.getId())); }
}
