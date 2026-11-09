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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auramed.auramed.controller.api.ApiDtos.AltaRequest;
import com.auramed.auramed.controller.api.ApiDtos.InternacaoRequest;
import com.auramed.auramed.controller.api.ApiDtos.InternacaoResponse;
import com.auramed.auramed.model.Internacao;
import com.auramed.auramed.service.InternacaoService;

@RestController
@RequestMapping("/internacoes")
public class InternacaoApiController {
    private final InternacaoService internacoes;

    public InternacaoApiController(InternacaoService internacoes) {
        this.internacoes = internacoes;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public List<InternacaoResponse> listar() {
        return internacoes.listar().stream().map(InternacaoResponse::from).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InternacaoResponse buscar(@PathVariable Long id) {
        return InternacaoResponse.from(internacoes.buscar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternacaoResponse> internar(@RequestBody InternacaoRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados da internação são obrigatórios");
        Internacao salva = internacoes.internar(dados.pacienteId(), dados.profissionalId(), dados.quartoId(),
            dados.dataEntrada(), dados.dataPrevistaAlta(), dados.observacoes());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(InternacaoResponse.from(salva));
    }

    @PatchMapping(value = "/{id}/alta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public InternacaoResponse darAlta(@PathVariable Long id, @RequestBody AltaRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados da alta são obrigatórios");
        return InternacaoResponse.from(internacoes.darAlta(id, dados.dataAlta(), dados.observacoes()));
    }
}
