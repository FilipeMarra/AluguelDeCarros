-- Tabela Contratante
CREATE TABLE Contratante (
    id SERIAL PRIMARY KEY, -- Incremento automático
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE, -- CPF único
    profissao VARCHAR(255),
    entidade VARCHAR(255)
);

-- Tabela Automovel
CREATE TABLE Automovel (
    id SERIAL PRIMARY KEY, -- Incremento automático
    modelo VARCHAR(255) NOT NULL,
    ano INT NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE -- Placa única
);

-- Tabela Aluguel
CREATE TABLE Aluguel (
    id SERIAL PRIMARY KEY, -- Incremento automático
    dataInicio DATE NOT NULL,
    dataFim DATE NOT NULL,
    numeroRegistro VARCHAR(50) NOT NULL UNIQUE, -- Número de registro único
    automovelID INT NOT NULL,
    contratanteID INT NOT NULL,
    FOREIGN KEY (automovelID) REFERENCES Automovel(id), -- Chave estrangeira para Automovel
    FOREIGN KEY (contratanteID) REFERENCES Contratante(id) -- Chave estrangeira para Contratante
);