# Projeto Acadêmico — Sistema Web

Projeto desenvolvido como trabalho acadêmico para a disciplina de **PROGRAMAÇÃO MODULAR** da **PUC Minas**.

O objetivo é aplicar, de forma prática, conceitos de desenvolvimento de software e desenvolvimento web abordados durante a disciplina.

## Tecnologias

* Java
* Spring Boot
* Thymeleaf
* HTML
* CSS
* Maven

## Execução

No diretório `auramed`, execute:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8081
```

## Contexto acadêmico

Este projeto possui finalidade exclusivamente acadêmica e foi desenvolvido de acordo com os requisitos e etapas definidos para o trabalho da disciplina.

## Modelo e serviços

O projeto usa entidades JPA para pacientes, profissionais da saúde, consultas, internações e quartos. Os serviços validam cadastros, impedem duas consultas agendadas para o mesmo profissional e horário, controlam vagas e altas sob transação e consultam o histórico médico do paciente. O histórico é calculado a partir das consultas realizadas e das internações, sem duplicar registros em outra tabela.

A escolha prevista para persistência é SQL Server: os dados têm relações e regras de integridade claras (paciente, profissional, consulta, internação e quarto), que podem ser protegidas por chaves estrangeiras e índices únicos. O driver já consta no `pom.xml`. O esquema correspondente está em `auramed/sql/schema.sql`; ele não é executado automaticamente. A conexão com o banco ainda precisa ser configurada antes de iniciar a aplicação com a camada JPA.

Para validar apenas o código Java, sem banco de dados:

```bash
cd auramed
./mvnw -DskipTests compile
```
