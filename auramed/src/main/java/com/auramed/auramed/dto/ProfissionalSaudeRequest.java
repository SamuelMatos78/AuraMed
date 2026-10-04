package com.auramed.auramed.dto;

import com.auramed.auramed.model.ProfissionalSaude;

public record ProfissionalSaudeRequest(String nome, String registroProfissional, String especialidade,
                                       String telefone, String email) {
    public ProfissionalSaude paraEntidade() {
        return new ProfissionalSaude(nome, registroProfissional, especialidade, telefone, email);
    }
}
