package com.auramed.auramed.controller;

import com.auramed.auramed.dto.ConsultaResponse;
import com.auramed.auramed.dto.ProfissionalSaudeRequest;
import com.auramed.auramed.dto.ProfissionalSaudeResponse;
import com.auramed.auramed.service.ProfissionalSaudeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalSaudeController {
    private final ProfissionalSaudeService service;
    public ProfissionalSaudeController(ProfissionalSaudeService service) { this.service = service; }

    @GetMapping
    public List<ProfissionalSaudeResponse> listar() {
        return service.listar().stream().map(ProfissionalSaudeResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ProfissionalSaudeResponse buscar(@PathVariable Long id) {
        return ProfissionalSaudeResponse.de(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ProfissionalSaudeResponse> cadastrar(@RequestBody ProfissionalSaudeRequest dados) {
        ProfissionalSaudeResponse criado = ProfissionalSaudeResponse.de(service.cadastrar(dados.paraEntidade()));
        return ResponseEntity.created(URI.create("/api/profissionais/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public ProfissionalSaudeResponse atualizar(@PathVariable Long id, @RequestBody ProfissionalSaudeRequest dados) {
        return ProfissionalSaudeResponse.de(service.atualizar(id, dados.paraEntidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/agenda")
    public List<ConsultaResponse> agenda(@PathVariable Long id, @RequestParam(required = false) LocalDate data) {
        return service.agenda(id, data == null ? LocalDate.now() : data).stream().map(ConsultaResponse::de).toList();
    }
}
