package com.auramed.auramed.dto;

import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.model.SituacaoQuarto;

public record QuartoResponse(Long id, String numero, Integer andar, Integer capacidadeMaxima,
                             long ocupacaoAtual, long vagasDisponiveis, SituacaoQuarto situacao) {
    public static QuartoResponse de(Quarto q, long ocupacao) {
        return new QuartoResponse(q.getId(), q.getNumero(), q.getAndar(), q.getCapacidadeMaxima(),
            ocupacao, q.getCapacidadeMaxima() - ocupacao, q.getSituacao());
    }
}
