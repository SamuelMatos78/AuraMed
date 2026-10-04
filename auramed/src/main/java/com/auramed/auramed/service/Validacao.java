package com.auramed.auramed.service;

import com.auramed.auramed.exception.DadosInvalidosException;

final class Validacao {
    private Validacao() {}

    static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new DadosInvalidosException(campo + " é obrigatório");
        return valor.trim();
    }

    static String email(String valor) {
        String email = obrigatorio(valor, "E-mail");
        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) throw new DadosInvalidosException("E-mail inválido");
        return email;
    }

    static Long id(Long id, String campo) {
        if (id == null || id <= 0) throw new DadosInvalidosException(campo + " inválido");
        return id;
    }
}
