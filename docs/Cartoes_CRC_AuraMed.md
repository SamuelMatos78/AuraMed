# Cartões CRC do AuraMed

CRC quer dizer **Classe, Responsabilidade e Colaboração**: o que cada classe sabe ou faz, e com quem ela trabalha.

O AuraMed cuida de pacientes, profissionais, consultas, internações, quartos e histórico médico. Os dados ficam nas **classes**. As regras ficam nos **serviços**.

## Classes (os dados)

| Classe | O que faz | Trabalha com |
|---|---|---|
| **Paciente** | Guarda nome, CPF, nascimento e contatos de quem é atendido. | PacienteService, Consulta, Internacao, HistoricoMedico |
| **ProfissionalSaude** | Guarda nome, registro, especialidade e contatos de quem atende. | ProfissionalSaudeService, Consulta, Internacao |
| **Consulta** | Guarda paciente, profissional, data, motivo, observações e o status. | Paciente, ProfissionalSaude, StatusConsulta, ConsultaService |
| **Internacao** | Guarda paciente, profissional, quarto, entrada, alta prevista e alta real. | Paciente, ProfissionalSaude, Quarto, InternacaoService |
| **Quarto** | Guarda número, andar, capacidade e se está livre ou ocupado. | SituacaoQuarto, Internacao, QuartoService |
| **HistoricoMedico** | Junta as consultas concluídas e as internações de um paciente. Não tem tabela própria. | Paciente, Consulta, Internacao, HistoricoMedicoService |

## Serviços (as regras)

| Serviço | O que faz | Trabalha com |
|---|---|---|
| **PacienteService** | Cadastra e atualiza pacientes. Confere CPF (11 dígitos), nascimento e CPF repetido. | Paciente, PacienteRepository |
| **ProfissionalSaudeService** | Cadastra e atualiza profissionais. Confere os campos e impede registro repetido. | ProfissionalSaude, ProfissionalSaudeRepository |
| **ConsultaService** | Marca consultas e muda o status, seguindo os passos permitidos. Impede dois horários iguais para o mesmo profissional. | Consulta, Paciente, ProfissionalSaude, ConsultaRepository |
| **InternacaoService** | Interna só se houver vaga e se o paciente não estiver internado. Dá alta uma única vez e atualiza o quarto. | Internacao, Paciente, ProfissionalSaude, Quarto |
| **QuartoService** | Cadastra quartos, confere número único e capacidade, e mostra as vagas. | Quarto, QuartoRepository, InternacaoRepository |
| **HistoricoMedicoService** | Monta o histórico do paciente, do mais novo para o mais antigo. | HistoricoMedico, Paciente, ConsultaRepository, InternacaoRepository |

## Bom saber

- Um quarto fica **OCUPADO** só quando lota. Com vaga sobrando, continua **DISPONIVEL**.
- Todos os serviços usam a classe `Validacao` para conferir os dados.

Versões para entrega: [PDF](entrega/Cartoes_CRC_AuraMed.pdf) e [DOCX](entrega/Cartoes_CRC_AuraMed.docx).
