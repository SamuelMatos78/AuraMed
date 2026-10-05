package com.auramed.auramed.model;

import java.time.LocalDateTime;

public record InternacaoResumo(Long id, String pacienteNome, String profissionalNome, String quartoNumero,
                               LocalDateTime dataEntrada, LocalDateTime dataPrevistaAlta,
                               LocalDateTime dataAlta, String observacoes) {
    public static InternacaoResumo de(Internacao i) {
        return new InternacaoResumo(i.getId(), i.getPaciente().getNome(), i.getProfissional().getNome(),
            i.getQuarto().getNumero(), i.getDataEntrada(), i.getDataPrevistaAlta(), i.getDataAlta(), i.getObservacoes());
    }
    public boolean isAtiva() { return dataAlta == null; }
}
