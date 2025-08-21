# BranchLift 🚀



## 🎯 Visão Geral

BranchLift é uma plataforma de orquestração de ambientes de desenvolvimento, inspirada em ferramentas de PaaS (Platform as a Service) como Vercel e Heroku. O objetivo é eliminar o atrito no ciclo de desenvolvimento, permitindo que desenvolvedores, QAs e stakeholders testem features de forma isolada e instantânea, criando ambientes de preview completos e descartáveis a partir de qualquer branch de um repositório Git.

### O Problema Resolvido

Em equipes de desenvolvimento modernas, o ciclo de vida de uma *feature branch* envolve múltiplos estágios de revisão e teste. O processo tradicional de "puxar a branch", resolver conflitos, construir localmente e executar testes para cada Pull Request é ineficiente e propenso a erros de "funciona na minha máquina".

BranchLift ataca esse problema de frente, automatizando a criação de um ambiente de execução completo e idêntico ao de produção (backend, frontend, banco de dados) para cada branch.

## ✨ Funcionalidades Principais

* **Gerenciamento Centralizado de Projetos:** Uma interface dedicada para cadastrar e configurar os repositórios Git que a plataforma deve gerenciar.
* **Provisionamento Dinâmico com um Clique:** A partir do dashboard, o usuário seleciona um projeto cadastrado, digita o nome de uma branch e inicia o provisionamento.
* **Orquestração de Ponta a Ponta:** O backend clona o repositório, cria um diretório de ambiente isolado e executa os comandos `docker-compose` para construir e iniciar toda a stack da aplicação.
* **Dashboard de Ambientes Ativos:** Um painel único para visualizar todos os ambientes em execução, seus status (`PROVISIONING`, `RUNNING`, `ERROR`), URLs de acesso geradas dinamicamente e informações de criação.
* **Isolamento Total:** Cada ambiente opera em sua própria rede Docker e utiliza um pool de portas gerenciado para evitar conflitos, permitindo que dezenas de ambientes coexistam no mesmo servidor.
* **Destruição Simplificada (Clean-up):** Com um único clique, o sistema executa um `docker-compose down`, removendo todos os contêineres, redes e volumes associados, e apaga o diretório do ambiente, liberando 100% dos recursos.

## 🛠️ Stack de Tecnologias

Esta plataforma foi construída com tecnologias modernas e robustas, focando em escalabilidade e manutenção.

| Camada              | Tecnologia                                                | Propósito                                                   |
| ------------------- | --------------------------------------------------------- | ----------------------------------------------------------- |
| **Backend** | Java 21, Spring Boot 3, Spring Data JPA                   | Lógica de negócio, orquestração de comandos e exposição de API |
| **Frontend** | Angular 20 (Standalone Components), TypeScript, RxJS      | Interface reativa, comunicação com o backend e dashboard     |
| **Banco de Dados** | PostgreSQL                                                | Persistência do estado dos projetos e ambientes             |
| **Containerização** | Docker & Docker Compose                                   | Empacotamento, isolamento e execução dos ambientes          |
| **Estilização** | Angular Material, CSS com Variáveis                       | UI moderna, responsiva e com tema escuro                     |

## ⚙️ Como Executar o Projeto Localmente

**Pré-requisitos:**
* Docker e Docker Compose instalados.
* Git instalado na máquina host.

**Passos:**

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/MateusMantovi/BranchLift.git](https://github.com/MateusMantovi/BranchLift.git)
    cd BranchLift
    ```

2.  **(Apenas na primeira vez ou em ambientes Linux) Permissão para o Docker Socket:**
    O contêiner do backend precisa de permissão para se comunicar com o motor Docker da sua máquina.
    ```bash
    # Para Linux/macOS/WSL
    sudo chmod 666 /var/run/docker.sock
    ```

3.  **Execute o Docker Compose:**
    Este comando irá construir as imagens do backend e frontend e iniciar todos os serviços.
    ```bash
    docker compose up --build -d
    ```

4.  **Acesse a Aplicação:**
    Aguarde cerca de um minuto para os serviços iniciarem.
    * A interface do BranchLift estará disponível em: **`http://localhost:8081`**

5.  **Comece a Usar:**
    * Navegue até a aba "Projetos" para cadastrar seu primeiro repositório.
    * Volte ao "Dashboard" para provisionar um ambiente a partir dele.

## 🗺️ Roadmap e Próximos Passos

Este projeto é uma base sólida com um grande potencial de evolução. Veja o nosso [BACKLOG.md](./BACKLOG.md) para uma lista detalhada de features planejadas, incluindo:

* **Feedback em Tempo Real:** Streaming dos logs de build para o frontend com WebSockets.
* **Autenticação e Autorização:** Sistema de login com JWT.
* **Ciclo de Vida Automático:** Ambientes que se autodestroem após um tempo pré-configurado.

## 🤝 Contribuições

Feedbacks, sugestões e contribuições são muito bem-vindos! Sinta-se à vontade para abrir uma *issue* ou enviar um *Pull Request*.

---