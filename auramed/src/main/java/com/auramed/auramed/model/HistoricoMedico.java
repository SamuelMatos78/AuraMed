package com.auramed.auramed.model;

import java.util.List;

/** Histórico derivado dos atendimentos; não é uma tabela independente. */
public record HistoricoMedico(Paciente paciente, List<Consulta> consultas, List<Internacao> internacoes) {}
