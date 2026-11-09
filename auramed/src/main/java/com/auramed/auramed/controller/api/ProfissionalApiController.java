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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auramed.auramed.controller.api.ApiDtos.ProfissionalRequest;
import com.auramed.auramed.controller.api.ApiDtos.ProfissionalResponse;
import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.service.ProfissionalSaudeService;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalApiController {
    private final ProfissionalSaudeService profissionais;

    public ProfissionalApiController(ProfissionalSaudeService profissionais) {
        this.profissionais = profissionais;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public List<ProfissionalResponse> listar(@RequestParam(required = false) String especialidade) {
        List<ProfissionalSaude> resultado = especialidade == null
            ? profissionais.listar() : profissionais.listarPorEspecialidade(especialidade);
        return resultado.stream().map(ProfissionalResponse::from).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfissionalResponse buscar(@PathVariable Long id) {
        return ProfissionalResponse.from(profissionais.buscar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfissionalResponse> cadastrar(@RequestBody ProfissionalRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do profissional são obrigatórios");
        ProfissionalSaude salvo = profissionais.cadastrar(dados.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(ProfissionalResponse.from(salvo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfissionalResponse atualizar(@PathVariable Long id, @RequestBody ProfissionalRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do profissional são obrigatórios");
        return ProfissionalResponse.from(profissionais.atualizar(id, dados.toModel()));
    }
}
