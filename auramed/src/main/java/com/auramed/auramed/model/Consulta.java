package com.auramed.auramed.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas", indexes = {
    @Index(name = "ix_consultas_paciente", columnList = "paciente_id"),
    @Index(name = "ix_consultas_profissional_horario", columnList = "profissional_id,data_hora")
})
public class Consulta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalSaude profissional;
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    @Column(nullable = false, length = 500)
    private String motivo;
    @Column(name = "observacoes_medicas", columnDefinition = "nvarchar(max)")
    private String observacoesMedicas;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    protected Consulta() {}
    public Consulta(Paciente paciente, ProfissionalSaude profissional, LocalDateTime dataHora, String motivo) {
        this.paciente = paciente; this.profissional = profissional; this.dataHora = dataHora; this.motivo = motivo;
    }
    public Long getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public ProfissionalSaude getProfissional() { return profissional; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getObservacoesMedicas() { return observacoesMedicas; }
    public void registrarObservacoes(String observacoesMedicas) {
        if (getStatus() != StatusConsulta.EM_ATENDIMENTO && getStatus() != StatusConsulta.CONCLUIDA)
            throw new IllegalStateException("Observações só podem ser registradas durante ou após o atendimento");
        this.observacoesMedicas = observacoesMedicas;
    }
    @Deprecated(forRemoval = false)
    public void setObservacoesMedicas(String observacoesMedicas) { registrarObservacoes(observacoesMedicas); }
    public StatusConsulta getStatus() { return status.normalizado(); }
    public void alterarStatus(StatusConsulta proximoStatus) {
        StatusConsulta atual = getStatus();
        StatusConsulta destino = proximoStatus == null ? null : proximoStatus.normalizado();
        if (!atual.permiteTransicaoPara(destino))
            throw new IllegalStateException("Transição de " + atual + " para " + destino + " não permitida");
        this.status = destino;
    }
    @Deprecated(forRemoval = false)
    public void setStatus(StatusConsulta proximoStatus) { alterarStatus(proximoStatus); }
}
