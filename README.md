# ⚽ Papitômetro Copa do Mundo 2026

Sistema full stack de bolão para a Copa do Mundo 2026. A aplicação permite criar salas, entrar com código de convite, registrar palpites por jogo, acompanhar partidas por data e visualizar o ranking dos participantes com pontuação automática.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=111)
![Vite](https://img.shields.io/badge/Vite-8-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

---

## ✨ Funcionalidades

- Cadastro e login de usuários com autenticação JWT
- Criação de salas para bolões privados
- Entrada em salas por código
- Listagem de jogos da Copa do Mundo por data
- Importação automática de partidas pela Football Data API
- Registro e edição de palpites antes do início dos jogos
- Visualização dos palpites dos participantes da sala
- Ranking por sala com pontuação automática
- Atualização periódica dos resultados oficiais
- Tratamento de placar sem somar disputa de pênaltis ao resultado do jogo

---

## 🧠 Regra de Pontuação

| Resultado do palpite | Pontos |
| --- | ---: |
| Placar exato | 3 |
| Acertou o vencedor ou empate | 1 |
| Errou o resultado | 0 |

Em jogos decididos nos pênaltis, o sistema considera apenas o placar de tempo normal + prorrogação. A disputa de pênaltis define o classificado, mas não entra no placar usado para pontuar os palpites.

---

## 🛠️ Tecnologias

### Back-end

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- H2 Database para testes/desenvolvimento local

### Front-end

- React
- Vite
- React Router
- Axios
- CSS

### API externa

- Football Data API

---

## 📁 Estrutura do Projeto

```text
papitometro-copa/
├── papitometroCopaDoMundo/     # Back-end Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── mvnw.cmd
│
├── papitometro-front/          # Front-end React + Vite
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

---

## ⚙️ Configuração

O back-end lê as principais configurações por variáveis de ambiente. Caso elas não sejam informadas, o projeto usa valores locais definidos em `application.properties`.

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `DATABASE_URL` | URL do banco PostgreSQL | `jdbc:postgresql://localhost:5432/papitometro_copa` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `postgres` |
| `JWT_SECRET` | Chave secreta para gerar tokens JWT | `sua-chave-secreta` |
| `JWT_EXPIRATION` | Tempo de expiração do token em ms | `86400000` |
| `FOOTBALL_API_TOKEN` | Token da Football Data API | `seu-token` |
| `FOOTBALL_API_URL` | URL de partidas da competição | `https://api.football-data.org/v4/competitions/WC/matches` |

No front-end, a URL da API pode ser configurada com:

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `VITE_API_URL` | URL base do back-end | `http://localhost:8080` |

---

## ▶️ Rodando o Projeto

### 1. Back-end

```bash
cd papitometroCopaDoMundo
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### 2. Front-end

```bash
cd papitometro-front
npm install
npm run dev
```

A aplicação ficará disponível em:

```text
http://localhost:5173
```

---

## 🔌 Principais Endpoints

### Usuários

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/usuarios` | Cadastra usuário |
| `POST` | `/usuarios/login` | Realiza login |
| `GET` | `/usuarios` | Lista usuários |
| `GET` | `/usuarios/{id}` | Busca usuário por ID |

### Salas

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/salas` | Cria uma sala |
| `POST` | `/salas/entrar` | Entra em uma sala por código |
| `GET` | `/salas/minhas/{usuarioId}` | Lista salas do usuário |

### Jogos

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/jogos` | Lista partidas |
| `GET` | `/jogos/{id}` | Busca partida por ID |
| `POST` | `/jogos` | Cadastra partida |
| `PUT` | `/jogos/{id}` | Atualiza partida |
| `DELETE` | `/jogos/{id}` | Remove partida |

### Palpites e Ranking

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/palpites` | Cria palpite |
| `PUT` | `/palpites/{id}` | Atualiza palpite |
| `GET` | `/palpites/usuario/{usuarioId}/sala/{salaId}` | Lista palpites do usuário na sala |
| `GET` | `/palpites/jogo/{jogoId}/sala/{salaId}` | Lista palpites de uma partida na sala |
| `GET` | `/palpites/ranking/sala/{salaId}` | Exibe ranking da sala |

---

## 🗃️ Entidades Principais

| Entidade | Responsabilidade |
| --- | --- |
| `Usuario` | Dados de acesso e identificação do participante |
| `Sala` | Agrupa participantes em um bolão privado |
| `UsuarioSala` | Relaciona usuários às salas |
| `Jogo` | Armazena partidas, times, escudos, data, status e placar |
| `Palpite` | Guarda o palpite do usuário, sala, jogo e pontuação |

---

## 🎯 Objetivo do Projeto

O Papitômetro foi criado para praticar desenvolvimento full stack com uma experiência próxima de um produto real: autenticação, consumo de API externa, persistência em banco, regras de negócio, ranking e integração completa entre front-end e back-end.

---

## 👨‍💻 Autor

Desenvolvido por **Gabriel Felix**.

Projeto criado para estudo e prática de Java, Spring Boot, React, APIs REST, banco de dados e desenvolvimento full stack.
