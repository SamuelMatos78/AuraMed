package com.auramed.auramed.dto;

import com.auramed.auramed.model.Quarto;

public record QuartoRequest(String numero, Integer andar, Integer capacidadeMaxima) {
    public Quarto paraEntidade() { return new Quarto(numero, andar, capacidadeMaxima); }
}
