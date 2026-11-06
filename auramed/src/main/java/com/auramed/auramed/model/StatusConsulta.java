package com.auramed.auramed.model;

public enum StatusConsulta {
    AGENDADA,
    CONFIRMADA,
    EM_ATENDIMENTO,
    CONCLUIDA,
    CANCELADA,
    NAO_COMPARECEU,
    @Deprecated(forRemoval = false)
    REALIZADA;

    public StatusConsulta normalizado() {
        return this == REALIZADA ? CONCLUIDA : this;
    }

    public boolean permiteTransicaoPara(StatusConsulta proximo) {
        if (proximo == null) return false;
        return switch (this) {
            case AGENDADA -> proximo == CONFIRMADA || proximo == CANCELADA || proximo == NAO_COMPARECEU;
            case CONFIRMADA -> proximo == EM_ATENDIMENTO || proximo == CANCELADA || proximo == NAO_COMPARECEU;
            case EM_ATENDIMENTO -> proximo == CONCLUIDA;
            case CONCLUIDA, CANCELADA, NAO_COMPARECEU, REALIZADA -> false;
        };
    }
}
