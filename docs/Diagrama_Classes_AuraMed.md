# Diagrama de classes do AuraMed

As classes do sistema e como elas se ligam.

**Cores:** verde = guardado no banco · azul = montado na hora · roxo = listas de opções

<!-- diagrama:inicio -->
```mermaid
classDiagram
direction TB
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
class HistoricoMedico {
    <<record>>
}
class SituacaoQuarto {
    <<enum>>
    DISPONIVEL
    OCUPADO
}
class StatusConsulta {
    <<enum>>
    AGENDADA
    CONFIRMADA
    EM_ATENDIMENTO
    CONCLUIDA
    CANCELADA
    NAO_COMPARECEU
    REALIZADA
}
Consulta "*" --> "1" Paciente
Consulta "*" --> "1" ProfissionalSaude
Consulta ..> StatusConsulta
Internacao "*" --> "1" Paciente
Internacao "*" --> "1" ProfissionalSaude
Internacao "*" --> "1" Quarto
Quarto ..> SituacaoQuarto
HistoricoMedico --> "1" Paciente
HistoricoMedico --> "*" Consulta
HistoricoMedico --> "*" Internacao
note for HistoricoMedico "Não tem tabela própria. É montado na hora."

classDef entidade fill:#d5f5d5,stroke:#2e8b2e,color:#000
classDef montado fill:#cfe8ff,stroke:#1d6fb8,color:#000
classDef opcoes fill:#e6d9f5,stroke:#7a4cb0,color:#000
class Consulta:::entidade
class Internacao:::entidade
class Paciente:::entidade
class ProfissionalSaude:::entidade
class Quarto:::entidade
class HistoricoMedico:::montado
class SituacaoQuarto:::opcoes
class StatusConsulta:::opcoes
```
<!-- diagrama:fim -->

**Lendo as setas:** `"*" --> "1"` quer dizer "muitos para um". Por exemplo, um paciente pode ter várias consultas, mas cada consulta é de um só paciente.

Este diagrama é gerado a partir do código. Depois de mudar uma classe em `model/`, rode `python3 scripts/gerar_diagrama_classes.py` na raiz do projeto.

Versões para entrega: [PDF](Diagrama_Classes_AuraMed.pdf) e [SVG](Diagrama_Classes_AuraMed.svg).
