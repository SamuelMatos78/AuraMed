# Diagramas — AuraMed

Diagramas em Mermaid, renderizados direto no GitHub. O diagrama de classes completo (com PDF e SVG) está em [Diagrama_Classes_AuraMed.mmd](Diagrama_Classes_AuraMed.mmd).

## Arquitetura em camadas

```mermaid
flowchart LR
    U([Navegador / cliente HTTP]) --> C1[AuraMedControl<br/>páginas Thymeleaf]
    U --> C2[Controllers REST<br/>controller/api]
    C1 --> S
    C2 --> S
    subgraph S[Serviços]
        direction TB
        PS[PacienteService]
        PrS[ProfissionalSaudeService]
        CS[ConsultaService]
        IS[InternacaoService]
        QS[QuartoService]
        HS[HistoricoMedicoService]
    end
    S --> R[Repositories<br/>Spring Data JPA]
    R --> DB[(SQL Server)]
```

## Agendamento de consulta

```mermaid
sequenceDiagram
    actor U as Usuário
    participant API as ConsultaApiController
    participant S as ConsultaService
    participant PR as PacienteRepository
    participant FR as ProfissionalSaudeRepository
    participant CR as ConsultaRepository

    U->>API: POST /consultas
    API->>S: agendar(pacienteId, profissionalId, dataHora, motivo)
    S->>S: valida data, motivo e ids
    S->>PR: findById(pacienteId)
    S->>FR: buscarComBloqueio(profissionalId)
    S->>CR: existe consulta ativa no horário?
    alt horário ocupado
        S-->>API: IllegalStateException
        API-->>U: erro
    else horário livre
        S->>CR: save(nova Consulta AGENDADA)
        S-->>API: Consulta
        API-->>U: consulta criada
    end
```

## Ciclo de vida da consulta

```mermaid
stateDiagram-v2
    [*] --> AGENDADA
    AGENDADA --> CONFIRMADA
    AGENDADA --> CANCELADA
    AGENDADA --> NAO_COMPARECEU
    CONFIRMADA --> EM_ATENDIMENTO
    CONFIRMADA --> CANCELADA
    CONFIRMADA --> NAO_COMPARECEU
    EM_ATENDIMENTO --> CONCLUIDA
    CONCLUIDA --> [*]
    CANCELADA --> [*]
    NAO_COMPARECEU --> [*]
```

`REALIZADA` é um valor antigo, mantido por compatibilidade e tratado como `CONCLUIDA`.

## Internação e alta

```mermaid
sequenceDiagram
    actor U as Usuário
    participant S as InternacaoService
    participant R as Repositories
    U->>S: internar(paciente, profissional, quarto, entrada, previstaAlta)
    S->>R: bloqueia paciente e quarto
    S->>R: paciente já internado? quarto cheio?
    alt regra violada
        S-->>U: erro (internação ativa ou quarto sem vagas)
    else ok
        S->>R: save(Internacao)
        S->>R: atualiza situação do quarto
        S-->>U: Internacao
    end
    U->>S: darAlta(id, dataAlta)
    S->>R: bloqueia internação e quarto
    S->>R: registra alta e libera vaga
    S-->>U: Internacao
```
