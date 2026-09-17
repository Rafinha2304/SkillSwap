# Roteiro de desenvolvimento - SkillSwap

Planejamento acordado pela equipe para os próximos ciclos, mantendo o formato de metas pequenas, cada uma acompanhada de teste, captura de tela e commit correspondente.

## Ciclo 3 - Habilidades, busca e sessão visível

- Sessão ativa no topo do site: exibir o nome do usuário logado na tela inicial, com atalho para o perfil e opção "Sair" (substituir os botões "Entrar" / "Criar conta" quando houver sessão).
- Registro de habilidades: cada estudante informa o que pode ensinar e o que deseja aprender, usando as tabelas `habilidades` e `usuario_habilidades` já criadas no `database/schema.sql`.
- Edição de habilidades dentro da página de perfil.
- Busca e listagem de estudantes por habilidade ou categoria.
- Visualização do perfil de outros estudantes (página pública de perfil).
- Substituir o aviso "A busca por habilidades será implementada no Ciclo 3" exibido no modal de categorias da página inicial.

## Ciclo 4 - Solicitações de troca

- Envio de solicitação de troca a partir do perfil de outro estudante.
- Aceite e recusa de solicitações.
- Estado das solicitações (pendentes, aceitas, recusadas) visível para os dois estudantes.

## Ciclo 5 - Painel, refinamento e apresentação

- Painel para acompanhamento das conexões e combinações realizadas.
- Refinamento visual e de usabilidade das telas existentes.
- Validação de sessão também no back-end (hoje a sessão vive no navegador) e demais ajustes de segurança.
- Testes finais de ponta a ponta e preparação da apresentação do projeto.
