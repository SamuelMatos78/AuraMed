package com.auramed.auramed.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quartos", uniqueConstraints = @UniqueConstraint(name = "uk_quartos_numero", columnNames = "numero"))
public class Quarto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String numero;
    @Column(nullable = false)
    private Integer andar;
    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoQuarto situacao = SituacaoQuarto.DISPONIVEL;

    protected Quarto() {}
    public Quarto(String numero, Integer andar, Integer capacidadeMaxima) {
        this.numero = numero; this.andar = andar; this.capacidadeMaxima = capacidadeMaxima;
    }
    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public Integer getAndar() { return andar; }
    public void setAndar(Integer andar) { this.andar = andar; }
    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(Integer capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    public SituacaoQuarto getSituacao() { return situacao; }
    public void setSituacao(SituacaoQuarto situacao) { this.situacao = situacao; }
}
