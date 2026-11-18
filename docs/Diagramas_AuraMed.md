# Diagramas do AuraMed

Aqui estão os desenhos que explicam como o sistema funciona. O GitHub mostra todos direto na página.

Quer ver as classes? Veja o [diagrama de classes](Diagrama_Classes_AuraMed.md). As responsabilidades de cada uma estão nos [cartões CRC](Cartoes_CRC_AuraMed.md).

**Cores:** azul = telas e pessoas · verde = regras do sistema · amarelo = banco de dados e perguntas · vermelho = erro

## 1. Como o sistema é organizado

Quem usa o sistema fala com os controllers. Eles chamam os serviços, que seguem as regras e guardam tudo no banco.

```mermaid
flowchart LR
    A["Pessoa usando"] --> B["Controllers<br/>recebem o pedido"]
    B --> C["Serviços<br/>aplicam as regras"]
    C --> D["Repositories<br/>falam com o banco"]
    D --> E[("SQL Server")]

    classDef tela fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef regra fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef banco fill:#fff3b8,stroke:#b8960c,color:#000
    class A,B tela
    class C,D regra
    class E banco
```

## 2. Marcar uma consulta

```mermaid
flowchart TD
    A(["Pedido de consulta"]) --> B{"Data e motivo<br/>preenchidos?"}
    B -- Não --> X["Erro: faltam dados"]
    B -- Sim --> C{"Paciente e profissional<br/>existem?"}
    C -- Não --> Y["Erro: não encontrado"]
    C -- Sim --> D{"Profissional livre<br/>nesse horário?"}
    D -- Não --> Z["Erro: horário ocupado"]
    D -- Sim --> E["Salva a consulta<br/>como AGENDADA"]
    E --> F(["Pronto!"])

    classDef inicio fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef ok fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef erro fill:#ffd6d6,stroke:#c0392b,color:#000
    classDef pergunta fill:#fff3b8,stroke:#b8960c,color:#000
    class A,F inicio
    class E ok
    class X,Y,Z erro
    class B,C,D pergunta
```

## 3. Os passos de uma consulta

Uma consulta só anda para frente. Não dá para voltar atrás.

```mermaid
flowchart LR
    A["AGENDADA"] --> B["CONFIRMADA"]
    B --> C["EM ATENDIMENTO"]
    C --> D["CONCLUÍDA"]
    A --> E["CANCELADA"]
    B --> E
    A --> F["NÃO COMPARECEU"]
    B --> F

    classDef inicio fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef meio fill:#fff3b8,stroke:#b8960c,color:#000
    classDef fim fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef ruim fill:#ffd6d6,stroke:#c0392b,color:#000
    class A inicio
    class B,C meio
    class D fim
    class E,F ruim
```

## 4. Internar um paciente

```mermaid
flowchart TD
    A(["Pedido de internação"]) --> B{"Paciente já está<br/>internado?"}
    B -- Sim --> X["Erro: já internado"]
    B -- Não --> C{"O quarto tem vaga?"}
    C -- Não --> Y["Erro: quarto cheio"]
    C -- Sim --> D["Salva a internação"]
    D --> E["Atualiza o quarto<br/>livre ou ocupado"]
    E --> F(["Pronto!"])

    classDef inicio fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef ok fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef erro fill:#ffd6d6,stroke:#c0392b,color:#000
    classDef pergunta fill:#fff3b8,stroke:#b8960c,color:#000
    class A,F inicio
    class D,E ok
    class X,Y erro
    class B,C pergunta
```

## 5. Dar alta

```mermaid
flowchart TD
    A(["Pedido de alta"]) --> B{"Internação ainda<br/>está aberta?"}
    B -- Não --> X["Erro: já teve alta"]
    B -- Sim --> C{"Alta depois<br/>da entrada?"}
    C -- Não --> Y["Erro: data errada"]
    C -- Sim --> D["Registra a alta"]
    D --> E["Libera a vaga no quarto"]
    E --> F(["Pronto!"])

    classDef inicio fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef ok fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef erro fill:#ffd6d6,stroke:#c0392b,color:#000
    classDef pergunta fill:#fff3b8,stroke:#b8960c,color:#000
    class A,F inicio
    class D,E ok
    class X,Y erro
    class B,C pergunta
```

## 6. Conversa entre as partes (marcar consulta)

```mermaid
sequenceDiagram
    actor U as Pessoa
    participant S as ConsultaService
    participant B as Banco
    U->>S: Quero marcar uma consulta
    rect rgb(213, 245, 213)
    S->>B: Paciente e profissional existem?
    B-->>S: Sim
    S->>B: Esse horário está livre?
    B-->>S: Sim
    end
    S->>B: Salvar consulta
    S-->>U: Consulta marcada!
```
