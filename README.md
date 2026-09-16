# SkillSwap

> Uma plataforma para estudantes compartilharem o que sabem e encontrarem pessoas para aprender algo novo.

O **SkillSwap** é uma aplicação web de aprendizagem colaborativa. A plataforma permitirá que estudantes cadastrem as habilidades que podem ensinar, indiquem o que desejam aprender e encontrem pessoas com interesses compatíveis para realizar trocas de conhecimento.

## Status do projeto

Em desenvolvimento - **Ciclo 1 concluído**.

Nesta primeira etapa foram desenvolvidos a estrutura inicial do projeto, a página de apresentação responsiva, a identidade visual e o modelo inicial do banco de dados.

## Funcionalidades planejadas

- Cadastro, login e perfil de usuário;
- Registro de habilidades oferecidas e desejadas;
- Busca de estudantes por habilidade ou categoria;
- Solicitações de troca, aceite e recusa;
- Painel para acompanhamento das conexões realizadas.

## Tecnologias

- Java 23;
- Spring Boot 3.5;
- HTML5, CSS3 e JavaScript;
- MySQL;
- Gradle.

## Como executar

1. Clone o repositório e abra a pasta do projeto.
2. Configure o Java 23 no terminal:

   ```powershell
   $env:JAVA_HOME='C:\Program Files\Java\jdk-23'
   $env:Path="$env:JAVA_HOME\bin;$env:Path"
   ```

3. Inicie a aplicação:

   ```powershell
   gradle bootRun
   ```

4. Acesse [http://localhost:8080](http://localhost:8080) no navegador.

Para encerrar o servidor, use `Ctrl + C` no terminal.

## Estrutura do projeto

```text
skillswap/
├── database/                 # Modelo inicial do banco de dados
├── src/main/java/            # Aplicação Java e Spring Boot
├── src/main/resources/static/# Interface web
└── README.md
```

## Equipe

- Rafael Kraieski
- Caio Hering
- Guilherme Bragagnolo

Projeto acadêmico desenvolvido para a disciplina de Desenvolvimento de Sistemas.
