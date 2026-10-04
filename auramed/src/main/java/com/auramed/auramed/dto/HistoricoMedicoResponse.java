package com.auramed.auramed.dto;

import com.auramed.auramed.model.HistoricoMedico;
import java.util.List;

public record HistoricoMedicoResponse(PacienteResponse paciente, List<ConsultaResponse> consultas,
                                      List<InternacaoResponse> internacoes) {
    public static HistoricoMedicoResponse de(HistoricoMedico h) {
        return new HistoricoMedicoResponse(PacienteResponse.de(h.paciente()),
            h.consultas().stream().map(ConsultaResponse::de).toList(),
            h.internacoes().stream().map(InternacaoResponse::de).toList());
    }
}
