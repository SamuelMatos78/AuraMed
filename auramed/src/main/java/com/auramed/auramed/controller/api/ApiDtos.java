package com.auramed.auramed.controller.api;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.model.Internacao;
import com.auramed.auramed.model.Paciente;
import com.auramed.auramed.model.ProfissionalSaude;
import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.model.SituacaoQuarto;
import com.auramed.auramed.model.StatusConsulta;

public final class ApiDtos {
    private ApiDtos() {}

    public record PacienteRequest(
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String endereco,
        String email
    ) {
        Paciente toModel() {
            return new Paciente(nome, cpf, dataNascimento, telefone, endereco, email);
        }
    }

    public record PacienteResponse(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String endereco,
        String email
    ) {
        static PacienteResponse from(Paciente paciente) {
            return new PacienteResponse(paciente.getId(), paciente.getNome(), paciente.getCpf(),
                paciente.getDataNascimento(), paciente.getTelefone(), paciente.getEndereco(), paciente.getEmail());
        }
    }

    public record ConsultaRequest(Long pacienteId, Long profissionalId, LocalDateTime dataHora, String motivo) {}

    public record ConsultaStatusRequest(StatusConsulta status, String observacoesMedicas) {}

    public record ConsultaResponse(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        LocalDateTime dataHora,
        String motivo,
        String observacoesMedicas,
        StatusConsulta status
    ) {
        static ConsultaResponse from(Consulta consulta) {
            return new ConsultaResponse(consulta.getId(), consulta.getPaciente().getId(), consulta.getPaciente().getNome(),
                consulta.getProfissional().getId(), consulta.getProfissional().getNome(), consulta.getDataHora(),
                consulta.getMotivo(), consulta.getObservacoesMedicas(), consulta.getStatus());
        }
    }

    public record InternacaoRequest(
        Long pacienteId,
        Long profissionalId,
        Long quartoId,
        LocalDateTime dataEntrada,
        LocalDateTime dataPrevistaAlta,
        String observacoes
    ) {}

    public record AltaRequest(LocalDateTime dataAlta, String observacoes) {}

    public record InternacaoResponse(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        Long quartoId,
        String quartoNumero,
        LocalDateTime dataEntrada,
        LocalDateTime dataPrevistaAlta,
        LocalDateTime dataAlta,
        String observacoes
    ) {
        static InternacaoResponse from(Internacao internacao) {
            return new InternacaoResponse(internacao.getId(), internacao.getPaciente().getId(), internacao.getPaciente().getNome(),
                internacao.getProfissional().getId(), internacao.getProfissional().getNome(),
                internacao.getQuarto().getId(), internacao.getQuarto().getNumero(), internacao.getDataEntrada(),
                internacao.getDataPrevistaAlta(), internacao.getDataAlta(), internacao.getObservacoes());
        }
    }

    public record ProfissionalRequest(
        String nome,
        String registroProfissional,
        String especialidade,
        String telefone,
        String email
    ) {
        ProfissionalSaude toModel() {
            return new ProfissionalSaude(nome, registroProfissional, especialidade, telefone, email);
        }
    }

    public record ProfissionalResponse(
        Long id,
        String nome,
        String registroProfissional,
        String especialidade,
        String telefone,
        String email
    ) {
        static ProfissionalResponse from(ProfissionalSaude profissional) {
            return new ProfissionalResponse(profissional.getId(), profissional.getNome(),
                profissional.getRegistroProfissional(), profissional.getEspecialidade(),
                profissional.getTelefone(), profissional.getEmail());
        }
    }

    public record QuartoRequest(String numero, Integer andar, Integer capacidadeMaxima) {
        Quarto toModel() {
            return new Quarto(numero, andar, capacidadeMaxima);
        }
    }

    public record QuartoResponse(
        Long id,
        String numero,
        Integer andar,
        Integer capacidadeMaxima,
        long vagasDisponiveis,
        SituacaoQuarto situacao
    ) {
        static QuartoResponse from(Quarto quarto, long vagasDisponiveis) {
            return new QuartoResponse(quarto.getId(), quarto.getNumero(), quarto.getAndar(),
                quarto.getCapacidadeMaxima(), vagasDisponiveis, quarto.getSituacao());
        }
    }

    public record ApiError(String erro) {}
}
