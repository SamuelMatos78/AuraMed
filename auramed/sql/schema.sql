-- SQL Server. Arquivo prototipo template. Execute manualmente no banco escolhido, quando a persistência for configurada.
-- As tabelas correspondem às entidades JPA em com.auramed.auramed.model.

CREATE TABLE dbo.pacientes (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome NVARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    endereco NVARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL,
    CONSTRAINT uk_pacientes_cpf UNIQUE (cpf)
);

CREATE TABLE dbo.profissionais_saude (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome NVARCHAR(150) NOT NULL,
    registro_profissional VARCHAR(50) NOT NULL,
    especialidade NVARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    CONSTRAINT uk_profissionais_registro UNIQUE (registro_profissional)
);

CREATE TABLE dbo.quartos (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    numero VARCHAR(20) NOT NULL,
    andar INT NOT NULL,
    capacidade_maxima INT NOT NULL,
    situacao VARCHAR(20) NOT NULL CONSTRAINT df_quartos_situacao DEFAULT 'DISPONIVEL',
    CONSTRAINT uk_quartos_numero UNIQUE (numero),
    CONSTRAINT ck_quartos_capacidade CHECK (capacidade_maxima > 0),
    CONSTRAINT ck_quartos_situacao CHECK (situacao IN ('DISPONIVEL', 'OCUPADO'))
);

CREATE TABLE dbo.consultas (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    data_hora DATETIME2(6) NOT NULL,
    motivo NVARCHAR(500) NOT NULL,
    observacoes_medicas NVARCHAR(MAX) NULL,
    status VARCHAR(20) NOT NULL CONSTRAINT df_consultas_status DEFAULT 'AGENDADA',
    CONSTRAINT fk_consultas_paciente FOREIGN KEY (paciente_id) REFERENCES dbo.pacientes(id),
    CONSTRAINT fk_consultas_profissional FOREIGN KEY (profissional_id) REFERENCES dbo.profissionais_saude(id),
    CONSTRAINT ck_consultas_status CHECK (status IN ('AGENDADA', 'REALIZADA', 'CANCELADA'))
);
CREATE INDEX ix_consultas_paciente ON dbo.consultas(paciente_id);
CREATE INDEX ix_consultas_profissional_horario ON dbo.consultas(profissional_id, data_hora);
CREATE UNIQUE INDEX ux_consultas_agendadas_profissional_horario
    ON dbo.consultas(profissional_id, data_hora) WHERE status = 'AGENDADA';

CREATE TABLE dbo.internacoes (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    quarto_id BIGINT NOT NULL,
    data_entrada DATETIME2(6) NOT NULL,
    data_prevista_alta DATETIME2(6) NOT NULL,
    data_alta DATETIME2(6) NULL,
    observacoes NVARCHAR(MAX) NULL,
    CONSTRAINT fk_internacoes_paciente FOREIGN KEY (paciente_id) REFERENCES dbo.pacientes(id),
    CONSTRAINT fk_internacoes_profissional FOREIGN KEY (profissional_id) REFERENCES dbo.profissionais_saude(id),
    CONSTRAINT fk_internacoes_quarto FOREIGN KEY (quarto_id) REFERENCES dbo.quartos(id),
    CONSTRAINT ck_internacoes_prevista CHECK (data_prevista_alta >= data_entrada),
    CONSTRAINT ck_internacoes_alta CHECK (data_alta IS NULL OR data_alta >= data_entrada)
);
CREATE INDEX ix_internacoes_paciente ON dbo.internacoes(paciente_id);
CREATE INDEX ix_internacoes_quarto_alta ON dbo.internacoes(quarto_id, data_alta);
CREATE UNIQUE INDEX ux_internacoes_paciente_ativo
    ON dbo.internacoes(paciente_id) WHERE data_alta IS NULL;
