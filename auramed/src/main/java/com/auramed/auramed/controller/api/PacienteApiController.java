package com.auramed.auramed.controller.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auramed.auramed.controller.api.ApiDtos.PacienteRequest;
import com.auramed.auramed.controller.api.ApiDtos.PacienteResponse;
import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteApiController {
    private final PacienteService pacientes;

    public PacienteApiController(PacienteService pacientes) {
        this.pacientes = pacientes;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public List<PacienteResponse> listar() {
        return pacientes.listar().stream().map(PacienteResponse::from).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PacienteResponse buscar(@PathVariable Long id) {
        return PacienteResponse.from(pacientes.buscar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody PacienteRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do paciente são obrigatórios");
        Paciente salvo = pacientes.cadastrar(dados.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(PacienteResponse.from(salvo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PacienteResponse atualizar(@PathVariable Long id, @RequestBody PacienteRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do paciente são obrigatórios");
        return PacienteResponse.from(pacientes.atualizar(id, dados.toModel()));
    }
}
