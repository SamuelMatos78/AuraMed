# Diagrama de classes do AuraMed

As classes do sistema e como elas se ligam.

**Cores:** 🟦 azul = pessoas · 🟩 verde = atendimentos · 🟨 amarelo = quarto · 🟪 roxo = listas de opções

```mermaid
classDiagram
direction TB
class Paciente {
    Long id
    String nome
    String cpf
    LocalDate dataNascimento
    String telefone
    String endereco
    String email
}
class ProfissionalSaude {
    Long id
    String nome
    String registroProfissional
    String especialidade
    String telefone
    String email
}
class Quarto {
    Long id
    String numero
    Integer andar
    Integer capacidadeMaxima
    SituacaoQuarto situacao
}
class Consulta {
    Long id
    LocalDateTime dataHora
    String motivo
    String observacoesMedicas
    StatusConsulta status
}
class Internacao {
    Long id
    LocalDateTime dataEntrada
    LocalDateTime dataPrevistaAlta
    LocalDateTime dataAlta
    String observacoes
}
class HistoricoMedico {
    <<record>>
}
class StatusConsulta {
    <<enum>>
    AGENDADA
    CONFIRMADA
    EM_ATENDIMENTO
    CONCLUIDA
    CANCELADA
    NAO_COMPARECEU
}
class SituacaoQuarto {
    <<enum>>
    DISPONIVEL
    OCUPADO
}
Consulta "*" --> "1" Paciente
Consulta "*" --> "1" ProfissionalSaude
Internacao "*" --> "1" Paciente
Internacao "*" --> "1" ProfissionalSaude
Internacao "*" --> "1" Quarto
HistoricoMedico --> "1" Paciente
HistoricoMedico --> "*" Consulta
HistoricoMedico --> "*" Internacao
Consulta ..> StatusConsulta
Quarto ..> SituacaoQuarto
note for HistoricoMedico "Não tem tabela própria. É montado na hora a partir das consultas concluídas e das internações."

classDef pessoa fill:#cfe8ff,stroke:#1d6fb8,color:#000
classDef atendimento fill:#d5f5d5,stroke:#2e8b2e,color:#000
classDef quarto fill:#fff3b8,stroke:#b8960c,color:#000
classDef opcoes fill:#e6d9f5,stroke:#7a4cb0,color:#000
class Paciente:::pessoa
class ProfissionalSaude:::pessoa
class Consulta:::atendimento
class Internacao:::atendimento
class HistoricoMedico:::atendimento
class Quarto:::quarto
class StatusConsulta:::opcoes
class SituacaoQuarto:::opcoes
```

**Lendo as setas:** `"*" --> "1"` quer dizer "muitos para um". Por exemplo, um paciente pode ter várias consultas, mas cada consulta é de um só paciente.

Versões para entrega: [PDF](Diagrama_Classes_AuraMed.pdf) e [SVG](Diagrama_Classes_AuraMed.svg).
