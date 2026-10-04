package com.auramed.auramed.dto;

import java.time.LocalDateTime;

public record InternacaoRequest(Long pacienteId, Long profissionalId, Long quartoId,
                                LocalDateTime dataEntrada, LocalDateTime dataPrevistaAlta, String observacoes) {}
