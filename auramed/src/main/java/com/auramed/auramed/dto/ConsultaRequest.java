package com.auramed.auramed.dto;

import com.auramed.auramed.exception.DadosInvalidosException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ConsultaRequest(Long pacienteId, Long profissionalId, LocalDate data, LocalTime horario, String motivo) {
    public LocalDateTime dataHora() {
        if (data == null || horario == null) throw new DadosInvalidosException("Data e horário são obrigatórios");
        return data.atTime(horario);
    }
}
