package com.auramed.auramed.controller;

import com.auramed.auramed.dto.ConsultaRequest;
import com.auramed.auramed.dto.ConsultaResponse;
import com.auramed.auramed.dto.RealizarConsultaRequest;
import com.auramed.auramed.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
    private final ConsultaService service;
    public ConsultaController(ConsultaService service) { this.service = service; }

    @GetMapping
    public List<ConsultaResponse> listar() { return service.listar().stream().map(ConsultaResponse::de).toList(); }

    @GetMapping("/{id}")
    public ConsultaResponse buscar(@PathVariable Long id) { return ConsultaResponse.de(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<ConsultaResponse> agendar(@RequestBody ConsultaRequest dados) {
        ConsultaResponse criada = ConsultaResponse.de(
            service.agendar(dados.pacienteId(), dados.profissionalId(), dados.dataHora(), dados.motivo()));
        return ResponseEntity.created(URI.create("/api/consultas/" + criada.id())).body(criada);
    }

    @PatchMapping("/{id}/realizar")
    public ConsultaResponse realizar(@PathVariable Long id, @RequestBody(required = false) RealizarConsultaRequest dados) {
        return ConsultaResponse.de(service.realizar(id, dados == null ? null : dados.observacoesMedicas()));
    }

    @PatchMapping("/{id}/cancelar")
    public ConsultaResponse cancelar(@PathVariable Long id) { return ConsultaResponse.de(service.cancelar(id)); }
}
