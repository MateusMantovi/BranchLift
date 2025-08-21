# Backlog do Projeto BranchLift 🚀

Este documento descreve o roadmap de desenvolvimento e as funcionalidades planejadas para o BranchLift. O backlog está organizado em "Epics", que são grandes blocos de funcionalidades, e "User Stories", que descrevem requisitos específicos sob a perspectiva do usuário.

---

## 🎯 Visão do Produto

Transformar o BranchLift em uma plataforma de "Ambientes como Serviço" (Environment as a Service) robusta, segura e eficiente, que acelera o ciclo de vida de desenvolvimento de software ao fornecer ambientes de preview instantâneos e descartáveis.

---

## 🗺️ Roadmap de Epics

| Prioridade | Epic                                      | Status      | Descrição                                                                         |
| :--------- | :---------------------------------------- | :---------- | :-------------------------------------------------------------------------------- |
| **P0** | **MVP - Gerenciamento Básico de Ambientes** | ✅ **Concluído** | Funcionalidades essenciais de cadastro de projetos e provisionamento/destruição.   |
| **P1** | **E01 - Feedback em Tempo Real** | 📝 **A Fazer** | Fornecer visibilidade ao vivo do processo de build para o usuário.                |
| **P2** | **E02 - Segurança e Multi-usuário** | 📝 **A Fazer** | Implementar autenticação, autorização e segregação de recursos por usuário.     |
| **P3** | **E03 - Otimização de Recursos** | 📝 **A Fazer** | Gerenciar o ciclo de vida dos ambientes para otimizar o uso do servidor.          |
| **P4** | **E04 - Integração e Notificações** | 💡 **Ideia** | Conectar o BranchLift a plataformas externas como GitHub e Slack.                |

---

##  backlog Detalhado

### ✅ Epic MVP: Gerenciamento Básico de Ambientes (Concluído)

-   **US01: Cadastro de Projetos:** Como usuário, quero cadastrar um projeto informando seu nome e a URL do repositório Git, para que o sistema saiba de onde puxar o código.
-   **US02: Listagem de Projetos:** Como usuário, quero ver uma lista de todos os projetos já cadastrados.
-   **US03: Provisionamento de Ambiente:** Como usuário, quero selecionar um projeto, informar uma branch e provisionar um novo ambiente para testar uma feature.
-   **US04: Dashboard de Ambientes:** Como usuário, quero ver uma lista de todos os ambientes ativos, seus status (Provisioning, Running, Error), e a URL de acesso.
-   **US05: Destruição de Ambiente:** Como usuário, quero poder destruir um ambiente para liberar os recursos do servidor.

### 📝 Epic E01: Feedback em Tempo Real (A Fazer)

* **US06: Streaming de Logs de Build:** Como usuário, ao provisionar um ambiente, quero ver os logs dos comandos `git clone` e `docker-compose up` em tempo real na minha tela, para entender o progresso e diagnosticar falhas rapidamente.
    * **Tarefa Técnica:** Criar um endpoint WebSocket no backend Spring.
    * **Tarefa Técnica:** O `EnvironmentService` deve capturar a saída do `ShellCommandExecutor` e enviá-la via WebSocket.
    * **Tarefa Técnica:** O frontend Angular deve se conectar ao WebSocket e exibir os logs em um componente de "terminal".
* **US07: Visualização de Logs de Erro:** Como usuário, se um ambiente falhar (status `ERROR`), quero poder clicar nele para ver o log completo do erro que foi salvo.

### 📝 Epic E02: Segurança e Multi-usuário (A Fazer)

* **US08: Cadastro e Login de Usuário:** Como um novo usuário, quero poder me cadastrar e fazer login no sistema para ter acesso ao dashboard.
* **US09: Proteção de API com JWT:** Como desenvolvedor do sistema, quero que todos os endpoints da API (exceto login/cadastro) sejam protegidos, exigindo um JSON Web Token válido.
    * **Tarefa Técnica:** Adicionar Spring Security e uma biblioteca JWT (ex: `jjwt`) ao backend.
    * **Tarefa Técnica:** Criar um `JwtAuthenticationFilter` para validar tokens.
    * **Tarefa Técnica:** O frontend deve salvar o token no `localStorage` após o login e enviá-lo no header `Authorization` de cada requisição.
* **US10: Segregação de Recursos:** Como usuário logado, quero ver e gerenciar apenas os projetos e ambientes que eu criei.

### 📝 Epic E03: Otimização de Recursos (A Fazer)

* **US11: Ciclo de Vida Automático (Auto-destruição):** Como usuário, ao criar um ambiente, quero poder definir um "tempo de vida" (ex: 8 horas), após o qual o ambiente será automaticamente destruído para economizar recursos.
    * **Tarefa Técnica:** Adicionar um campo `expiresAt` (LocalDateTime) na entidade `Environment`.
    * **Tarefa Técnica:** Criar um método agendado no Spring (`@Scheduled`) que roda periodicamente para verificar e destruir ambientes expirados.
* **US12: Gerenciamento de Pool de Portas:** Como administrador do sistema, quero que o BranchLift gerencie um pool de portas disponíveis para evitar conflitos quando múltiplos ambientes são criados simultaneamente.

### 💡 Epic E04: Integração e Notificações (Ideias)

* **US13: Integração com GitHub Webhooks:** Como usuário, quero que um ambiente seja criado automaticamente toda vez que um novo Pull Request for aberto em um projeto cadastrado.
* **US14: Notificações no Slack:** Como usuário, quero receber uma notificação no Slack quando um ambiente que eu solicitei ficar pronto (status `RUNNING`) ou falhar (status `ERROR`), contendo a URL de acesso ou o log do erro.

---