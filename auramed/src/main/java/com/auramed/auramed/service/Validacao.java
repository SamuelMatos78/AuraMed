package com.auramed.auramed.service;

final class Validacao {
    private Validacao() {}

    static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo + " é obrigatório");
        return valor.trim();
    }

    static Long id(Long id, String campo) {
        if (id == null || id <= 0) throw new IllegalArgumentException(campo + " inválido");
        return id;
    }
}
