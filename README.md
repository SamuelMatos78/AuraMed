# AuraMed

Sistema web para cuidar de **pacientes, consultas e internações** de um hospital.

Trabalho da disciplina de Programação Modular, na PUC Minas.

## O que ele faz

- Cadastra pacientes e profissionais da saúde.
- Marca consultas, sem deixar dois horários iguais para o mesmo profissional.
- Interna pacientes só se o quarto tiver vaga, e registra a alta.
- Mostra o histórico de cada paciente.

## Feito com

Java, Spring Boot, Thymeleaf, HTML, CSS e Maven.

## Como rodar

Na pasta `auramed`:

```bash
./mvnw spring-boot:run
```

Depois abra `http://localhost:8081`.

Só para conferir se o código compila (sem banco de dados):

```bash
cd auramed
./mvnw -DskipTests compile
```

## Banco de dados

Usamos SQL Server. A estrutura está em `auramed/sql/schema.sql`, mas ela **não roda sozinha**. Falta configurar a conexão antes de usar o banco.

## Como funciona, em desenhos

- [Diagramas e fluxogramas](docs/Diagramas_AuraMed.md): como o sistema é organizado, marcar consulta, internar e dar alta.
- [Diagrama de classes](docs/Diagrama_Classes_AuraMed.md): as classes e como se ligam.
- [Cartões CRC](docs/Cartoes_CRC_AuraMed.md): o que cada classe faz.

```mermaid
flowchart LR
    A["Pessoa"] --> B["Controllers"] --> C["Serviços"] --> D["Repositories"] --> E[("Banco")]
    classDef tela fill:#cfe8ff,stroke:#1d6fb8,color:#000
    classDef regra fill:#d5f5d5,stroke:#2e8b2e,color:#000
    classDef banco fill:#fff3b8,stroke:#b8960c,color:#000
    class A,B tela
    class C,D regra
    class E banco
```
