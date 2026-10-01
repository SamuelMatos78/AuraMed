package com.auramed.auramed.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes", uniqueConstraints = @UniqueConstraint(name = "uk_pacientes_cpf", columnNames = "cpf"))
public class Paciente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, length = 11)
    private String cpf;
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(nullable = false, length = 255)
    private String endereco;
    @Column(nullable = false, length = 150)
    private String email;

    protected Paciente() {}
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String endereco, String email) {
        this.nome = nome; this.cpf = cpf; this.dataNascimento = dataNascimento;
        this.telefone = telefone; this.endereco = endereco; this.email = email;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
