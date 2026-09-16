CREATE DATABASE IF NOT EXISTS skillswap;
USE skillswap;

CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    biografia VARCHAR(500),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE habilidades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    descricao VARCHAR(300)
);

CREATE TABLE usuario_habilidades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    habilidade_id BIGINT NOT NULL,
    tipo ENUM('OFERECE', 'DESEJA_APRENDER') NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (habilidade_id) REFERENCES habilidades(id)
);
