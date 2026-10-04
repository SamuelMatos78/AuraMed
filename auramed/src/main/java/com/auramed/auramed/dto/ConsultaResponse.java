package com.auramed.auramed.dto;

import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.model.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaResponse(Long id, Long pacienteId, String pacienteNome, Long profissionalId, String profissionalNome,
                               LocalDate data, LocalTime horario, String motivo, String observacoesMedicas,
                               StatusConsulta status) {
    public static ConsultaResponse de(Consulta c) {
        return new ConsultaResponse(c.getId(), c.getPaciente().getId(), c.getPaciente().getNome(),
            c.getProfissional().getId(), c.getProfissional().getNome(),
            c.getDataHora().toLocalDate(), c.getDataHora().toLocalTime(), c.getMotivo(),
            c.getObservacoesMedicas(), c.getStatus());
    }
}
