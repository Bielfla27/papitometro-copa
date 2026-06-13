# ⚽ Papitômetro Copa do Mundo 2026

Sistema de bolão da Copa do Mundo 2026 desenvolvido com **Spring Boot** e **React**, permitindo que usuários realizem palpites dos jogos e acompanhem o ranking em tempo real.

---

## 🚀 Funcionalidades

* Importação automática dos jogos da Copa do Mundo através da Football Data API
* Exibição das partidas organizadas por data
* Cadastro de palpites por usuário
* Ranking de participantes
* Atualização automática dos resultados oficiais
* Integração completa entre Front-end e Back-end

---

## 🛠 Tecnologias Utilizadas

### Back-end

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven
* H2 Database

### Front-end

* React
* Vite
* Axios
* CSS

### API Externa

* Football Data API

---

## 📂 Estrutura do Projeto

```text
papitometro-copa
│
├── backend
│   ├── src
│   ├── pom.xml
│   └── ...
│
├── frontend
│   ├── src
│   ├── public
│   ├── package.json
│   └── ...
│
└── README.md
```

---

## ⚙️ Executando o Back-end

Acesse a pasta do backend:

```bash
cd backend
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

---

## 💻 Executando o Front-end

Acesse a pasta do frontend:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Execute o projeto:

```bash
npm run dev
```

O sistema ficará disponível em:

```text
http://localhost:5173
```

---

## 📊 Entidades Principais

### Usuário

* id
* nome
* email
* senha

### Jogo

* id
* apiId
* timeCasa
* timeFora
* escudoTimeCasa
* escudoTimeFora
* dataHora
* golsCasa
* golsFora

### Palpite

* id
* usuario
* jogo
* golsCasa
* golsFora
* pontos

---

## 🎯 Objetivo

Criar uma plataforma simples e intuitiva para organizar bolões da Copa do Mundo entre amigos, permitindo acompanhar partidas, registrar palpites e calcular automaticamente a pontuação dos participantes.

---

## 👨‍💻 Autor

Gabriel Felix

Projeto desenvolvido para estudo e prática de:

* Java Spring Boot
* React
* Integração de APIs REST
* Banco de Dados
* Desenvolvimento Full Stack
