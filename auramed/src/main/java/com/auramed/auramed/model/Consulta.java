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
    public void setObservacoesMedicas(String observacoesMedicas) { this.observacoesMedicas = observacoesMedicas; }
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }
}
