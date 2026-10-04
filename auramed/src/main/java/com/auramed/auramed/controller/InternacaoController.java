package com.auramed.auramed.controller;

import com.auramed.auramed.dto.AltaRequest;
import com.auramed.auramed.dto.InternacaoRequest;
import com.auramed.auramed.dto.InternacaoResponse;
import com.auramed.auramed.service.InternacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/internacoes")
public class InternacaoController {
    private final InternacaoService service;
    public InternacaoController(InternacaoService service) { this.service = service; }

    @GetMapping
    public List<InternacaoResponse> listar(@RequestParam(defaultValue = "false") boolean ativas) {
        return (ativas ? service.listarAtivas() : service.listar()).stream().map(InternacaoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public InternacaoResponse buscar(@PathVariable Long id) { return InternacaoResponse.de(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<InternacaoResponse> internar(@RequestBody InternacaoRequest dados) {
        InternacaoResponse criada = InternacaoResponse.de(service.internar(dados.pacienteId(), dados.profissionalId(),
            dados.quartoId(), dados.dataEntrada(), dados.dataPrevistaAlta(), dados.observacoes()));
        return ResponseEntity.created(URI.create("/api/internacoes/" + criada.id())).body(criada);
    }

    @PatchMapping("/{id}/alta")
    public InternacaoResponse darAlta(@PathVariable Long id, @RequestBody AltaRequest dados) {
        return InternacaoResponse.de(service.darAlta(id, dados.dataAlta(), dados.observacoes()));
    }
}
