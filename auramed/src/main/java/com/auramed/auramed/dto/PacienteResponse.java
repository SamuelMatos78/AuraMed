package com.auramed.auramed.dto;

import com.auramed.auramed.model.Paciente;
import java.time.LocalDate;

public record PacienteResponse(Long id, String nome, String cpf, LocalDate dataNascimento,
                               String telefone, String endereco, String email) {
    public static PacienteResponse de(Paciente p) {
        return new PacienteResponse(p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento(),
            p.getTelefone(), p.getEndereco(), p.getEmail());
    }
}
