package com.auramed.auramed.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profissionais_saude", uniqueConstraints = @UniqueConstraint(name = "uk_profissionais_registro", columnNames = "registro_profissional"))
public class ProfissionalSaude {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(name = "registro_profissional", nullable = false, length = 50)
    private String registroProfissional;
    @Column(nullable = false, length = 100)
    private String especialidade;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(nullable = false, length = 150)
    private String email;

    protected ProfissionalSaude() {}
    public ProfissionalSaude(String nome, String registroProfissional, String especialidade, String telefone, String email) {
        this.nome = nome; this.registroProfissional = registroProfissional;
        this.especialidade = especialidade; this.telefone = telefone; this.email = email;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRegistroProfissional() { return registroProfissional; }
    public void setRegistroProfissional(String registroProfissional) { this.registroProfissional = registroProfissional; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
