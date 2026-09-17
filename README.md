# SkillSwap

> Uma plataforma para estudantes compartilharem o que sabem e encontrarem pessoas para aprender algo novo.

O **SkillSwap** é uma aplicação web de aprendizagem colaborativa. A plataforma permitirá que estudantes cadastrem as habilidades que podem ensinar, indiquem o que desejam aprender e encontrem pessoas com interesses compatíveis para realizar trocas de conhecimento.

## Status do projeto

**Ciclo 5 concluído — projeto completo.**

- **Ciclo 1:** estrutura inicial do projeto, página de apresentação responsiva, identidade visual e modelo inicial do banco de dados.
- **Ciclo 2:** conexão real com o MySQL e funcionalidades de conta: cadastro, login, perfil do usuário e logout.
- **Ciclo 3:** registro de habilidades (posso ensinar / quero aprender), busca de estudantes por habilidade ou categoria, perfil público de outros estudantes e sessão ativa no topo do site.
- **Ciclo 4:** solicitações de troca de conhecimento: envio a partir do perfil de outro estudante, aceite e recusa pelo destinatário e acompanhamento do estado de cada solicitação.
- **Ciclo 5:** painel de conexões com resumo das solicitações, validação de sessão no back-end por token de acesso e refinamentos finais (favicon e ajustes de usabilidade).

## Funcionalidades

- Página de apresentação responsiva com identidade visual;
- Cadastro de usuário com senha protegida por hash (BCrypt);
- Login com validação de e-mail e senha e emissão de token de sessão;
- Perfil do usuário com edição de nome, biografia e habilidades;
- Busca de estudantes por habilidade ou categoria;
- Perfil público de outros estudantes;
- Sessão ativa no topo do site (nome do usuário logado, atalho para o perfil e sair);
- Solicitações de troca: envio, aceite, recusa e acompanhamento do estado;
- Painel "Minhas trocas" com resumo das conexões;
- Ações de escrita protegidas no back-end por token de sessão.

## Tecnologias

- Java 21;
- Spring Boot 3.5 (Web, Data JPA);
- HTML5, CSS3 e JavaScript;
- MySQL 8.0;
- Gradle 9.

## Como executar

1. Clone o repositório e abra a pasta do projeto.
2. Tenha o MySQL 8.0 instalado e em execução, e crie o banco com o script `database/schema.sql`.
3. Configure o Java 21 no terminal:

   ```powershell
   $env:JAVA_HOME='C:\Program Files\Java\jdk-21'
   $env:Path="$env:JAVA_HOME\bin;$env:Path"
   ```

4. Ajuste a senha do MySQL em `src/main/resources/application.properties` (ou defina a variável de ambiente `DB_PASSWORD`).
5. Inicie a aplicação:

   ```powershell
   .\gradlew bootRun
   ```

6. Acesse [http://localhost:8080](http://localhost:8080) no navegador.

Para encerrar o servidor, use `Ctrl + C` no terminal.

## Estrutura do projeto

```text
skillswap/
├── database/                 # Script de criação do banco de dados
├── src/main/java/            # Aplicação Java e Spring Boot
│   └── br/com/skillswap/
│       ├── usuario/          # Entidade, repositório, serviço e API de usuários
│       ├── habilidade/       # Habilidades, vínculos com usuários, perfil e busca
│       └── dto/              # Objetos de transferência de dados
├── src/main/resources/static/# Interface web (início, cadastro, login, perfil, busca)
└── README.md
```

O roteiro planejado para os próximos ciclos está em `docs/ROTEIRO_CICLOS.md`.

## Equipe

- Rafael Kraieski
- Caio Hering

Projeto acadêmico desenvolvido para a disciplina de Desenvolvimento de Sistemas.
