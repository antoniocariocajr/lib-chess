# Contribuindo para lib-chess

Obrigado por considerar contribuir com o lib-chess! Abaixo estão orientações para facilitar seu trabalho.

1. Passos iniciais

- Faça um fork do repositório e crie uma branch a partir de `main` com um nome descritivo (ex: `feat/fen-parser`, `fix/move-generation`).
- Execute os testes locais com: `mvn test` e garanta que tudo passe antes de enviar o PR.

1. Como abrir um issue

- Antes de abrir, verifique issues existentes para evitar duplicação.
- Para reports de bugs inclua:
  - Descrição do problema e comportamento esperado.
  - Passos para reproduzir.
  - Versão do Java e saída/stacktrace (se houver).
  - FEN ou PGN de exemplo quando aplicável.

1. Como submeter um Pull Request

- Faça alterações em uma branch separada do fork.
- Escreva uma descrição clara do que o PR corrige ou adiciona.
- Vincule o PR ao issue correspondente (ex: `Closes #12`) quando apropriado.
- Inclua testes que validem a mudança (unit tests / perft tests).
- Mantenha commits pequenos e com mensagens descritivas.

1. Estilo de código

- Siga as regras definidas pelo Checkstyle do projeto (quando configurado).
- Favor usar convenções Java (nomes camelCase, classes em PascalCase).
- Prefira métodos curtos e responsabilidades únicas.

1. Testes

- Adicione testes unitários para novos comportamentos e correções.
- Para funcionalidades de geração de movimentos, considere adicionar testes perft para garantir contagem de nós.

1. Revisão e feedback

- Seja receptivo a revisão de código e comentários.
- Se seu PR for grande, considere quebrá-lo em PRs menores.

1. Licença

- Ao enviar um PR você concorda em submeter seu código sob a licença do projeto (veja LICENSE).

Agradecemos pela colaboração — cada contribuição ajuda a melhorar a biblioteca!
