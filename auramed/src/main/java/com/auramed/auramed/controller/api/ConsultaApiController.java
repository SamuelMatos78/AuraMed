package com.auramed.auramed.controller.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auramed.auramed.controller.api.ApiDtos.ConsultaRequest;
import com.auramed.auramed.controller.api.ApiDtos.ConsultaResponse;
import com.auramed.auramed.controller.api.ApiDtos.ConsultaStatusRequest;
import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaApiController {
    private final ConsultaService consultas;

    public ConsultaApiController(ConsultaService consultas) {
        this.consultas = consultas;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public List<ConsultaResponse> listar(@RequestParam(required = false) Long pacienteId) {
        List<Consulta> resultado = pacienteId == null ? consultas.listar() : consultas.listarPorPaciente(pacienteId);
        return resultado.stream().map(ConsultaResponse::from).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponse buscar(@PathVariable Long id) {
        return ConsultaResponse.from(consultas.buscar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultaResponse> agendar(@RequestBody ConsultaRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados da consulta são obrigatórios");
        Consulta salva = consultas.agendar(dados.pacienteId(), dados.profissionalId(), dados.dataHora(), dados.motivo());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(ConsultaResponse.from(salva));
    }

    @PatchMapping(value = "/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponse alterarStatus(@PathVariable Long id, @RequestBody ConsultaStatusRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Status da consulta é obrigatório");
        return ConsultaResponse.from(consultas.alterarStatus(id, dados.status(), dados.observacoesMedicas()));
    }
}
