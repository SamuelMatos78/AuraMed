package com.auramed.auramed.dto;

import com.auramed.auramed.model.ProfissionalSaude;

public record ProfissionalSaudeResponse(Long id, String nome, String registroProfissional, String especialidade,
                                        String telefone, String email) {
    public static ProfissionalSaudeResponse de(ProfissionalSaude p) {
        return new ProfissionalSaudeResponse(p.getId(), p.getNome(), p.getRegistroProfissional(),
            p.getEspecialidade(), p.getTelefone(), p.getEmail());
    }
}
