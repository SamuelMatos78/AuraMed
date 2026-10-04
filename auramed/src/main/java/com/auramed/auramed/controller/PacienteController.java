package com.auramed.auramed.controller;

import com.auramed.auramed.dto.HistoricoMedicoResponse;
import com.auramed.auramed.dto.PacienteRequest;
import com.auramed.auramed.dto.PacienteResponse;
import com.auramed.auramed.service.HistoricoMedicoService;
import com.auramed.auramed.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    private final PacienteService service;
    private final HistoricoMedicoService historico;
    public PacienteController(PacienteService service, HistoricoMedicoService historico) {
        this.service = service; this.historico = historico;
    }

    @GetMapping
    public List<PacienteResponse> listar() {
        return service.listar().stream().map(PacienteResponse::de).toList();
    }

    @GetMapping("/{id}")
    public PacienteResponse buscar(@PathVariable Long id) { return PacienteResponse.de(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody PacienteRequest dados) {
        PacienteResponse criado = PacienteResponse.de(service.cadastrar(dados.paraEntidade()));
        return ResponseEntity.created(URI.create("/api/pacientes/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public PacienteResponse atualizar(@PathVariable Long id, @RequestBody PacienteRequest dados) {
        return PacienteResponse.de(service.atualizar(id, dados.paraEntidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historico")
    public HistoricoMedicoResponse historico(@PathVariable Long id) {
        return HistoricoMedicoResponse.de(historico.consultar(id));
    }
}
