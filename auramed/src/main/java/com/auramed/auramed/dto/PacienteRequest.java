package com.auramed.auramed.dto;

import com.auramed.auramed.model.Paciente;
import java.time.LocalDate;

public record PacienteRequest(String nome, String cpf, LocalDate dataNascimento,
                              String telefone, String endereco, String email) {
    public Paciente paraEntidade() {
        return new Paciente(nome, cpf, dataNascimento, telefone, endereco, email);
    }
}
