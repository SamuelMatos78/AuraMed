package com.auramed.auramed.dto;

import com.auramed.auramed.model.Internacao;
import java.time.LocalDateTime;

public record InternacaoResponse(Long id, Long pacienteId, String pacienteNome, Long profissionalId, String profissionalNome,
                                 Long quartoId, String quartoNumero, LocalDateTime dataEntrada,
                                 LocalDateTime dataPrevistaAlta, LocalDateTime dataAlta, String observacoes, boolean ativa) {
    public static InternacaoResponse de(Internacao i) {
        return new InternacaoResponse(i.getId(), i.getPaciente().getId(), i.getPaciente().getNome(),
            i.getProfissional().getId(), i.getProfissional().getNome(),
            i.getQuarto().getId(), i.getQuarto().getNumero(), i.getDataEntrada(),
            i.getDataPrevistaAlta(), i.getDataAlta(), i.getObservacoes(), i.getDataAlta() == null);
    }
}
