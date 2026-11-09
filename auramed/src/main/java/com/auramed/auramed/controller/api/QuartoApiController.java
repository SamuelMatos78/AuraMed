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

import com.auramed.auramed.controller.api.ApiDtos.QuartoRequest;
import com.auramed.auramed.controller.api.ApiDtos.QuartoResponse;
import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.service.QuartoService;

@RestController
@RequestMapping("/quartos")
public class QuartoApiController {
    private final QuartoService quartos;

    public QuartoApiController(QuartoService quartos) {
        this.quartos = quartos;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public List<QuartoResponse> listar(@RequestParam(defaultValue = "false") boolean comVaga) {
        return quartos.listar().stream()
            .map(this::paraResposta)
            .filter(q -> !comVaga || q.vagasDisponiveis() > 0)
            .toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuartoResponse buscar(@PathVariable Long id) {
        return paraResposta(quartos.buscar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuartoResponse> cadastrar(@RequestBody QuartoRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do quarto são obrigatórios");
        Quarto salvo = quartos.cadastrar(dados.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(paraResposta(salvo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QuartoResponse atualizar(@PathVariable Long id, @RequestBody QuartoRequest dados) {
        if (dados == null) throw new IllegalArgumentException("Dados do quarto são obrigatórios");
        return paraResposta(quartos.atualizar(id, dados.toModel()));
    }

    private QuartoResponse paraResposta(Quarto quarto) {
        return QuartoResponse.from(quarto, quartos.vagasDisponiveis(quarto.getId()));
    }
}
