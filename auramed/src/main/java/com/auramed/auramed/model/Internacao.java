package com.auramed.auramed.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internacoes", indexes = {
    @Index(name = "ix_internacoes_paciente", columnList = "paciente_id"),
    @Index(name = "ix_internacoes_quarto_alta", columnList = "quarto_id,data_alta")
})
public class Internacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalSaude profissional;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
    @Column(name = "data_prevista_alta", nullable = false)
    private LocalDateTime dataPrevistaAlta;
    @Column(name = "data_alta")
    private LocalDateTime dataAlta;
    @Column(columnDefinition = "nvarchar(max)")
    private String observacoes;

    protected Internacao() {}
    public Internacao(Paciente paciente, ProfissionalSaude profissional, Quarto quarto,
                     LocalDateTime dataEntrada, LocalDateTime dataPrevistaAlta, String observacoes) {
        this.paciente = paciente; this.profissional = profissional; this.quarto = quarto;
        this.dataEntrada = dataEntrada; this.dataPrevistaAlta = dataPrevistaAlta; this.observacoes = observacoes;
    }
    public Long getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public ProfissionalSaude getProfissional() { return profissional; }
    public Quarto getQuarto() { return quarto; }
    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public LocalDateTime getDataPrevistaAlta() { return dataPrevistaAlta; }
    public void setDataPrevistaAlta(LocalDateTime dataPrevistaAlta) { this.dataPrevistaAlta = dataPrevistaAlta; }
    public LocalDateTime getDataAlta() { return dataAlta; }
    public void setDataAlta(LocalDateTime dataAlta) { this.dataAlta = dataAlta; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
