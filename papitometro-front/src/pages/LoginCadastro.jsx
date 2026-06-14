import { useState } from "react";
import api from "../services/api";
import "./LoginCadastro.css";
import logoCopa from "../assets/logo-copa.png";

function LoginCadastro({ onLogin }) {
  const [modoCadastro, setModoCadastro] = useState(false);
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");

  async function cadastrarUsuario(e) {
        e.preventDefault();

        try {
            const response = await api.post("/usuarios", {
            nome,
            email,
            senha,
            });

            localStorage.setItem("usuarioLogado", JSON.stringify(response.data));
            onLogin(response.data);
        } catch (error) {
            console.error(error);
            alert("Erro ao cadastrar usuário.");
        }
    }

   /* function entrar(e) {
        e.preventDefault();

        // Login simples temporário
        const usuarioFake = {
        id: 1,
        nome: nome || "Gabriel",
        email,
        };

        localStorage.setItem("usuarioLogado", JSON.stringify(usuarioFake));
        onLogin(usuarioFake);
    }*/


     async function entrar(e) {
    e.preventDefault();

    try {
        const response = await api.post("/usuarios/login", {
        email,
        senha,
        });

        localStorage.setItem("usuarioLogado", JSON.stringify(response.data));
        onLogin(response.data);
    } catch (error) {
        console.error(error);
        alert("Email ou senha inválidos.");
    }
    }

  return (
    <main className="auth-page">
      <section className="auth-card">
        <div className="auth-logo">
             <img src={logoCopa} alt="FelixPlay Copa" />
        </div>

        <h1>Papitômetro 2026</h1>
        <p>Entre no bolão da Copa do Mundo</p>

        <div className="auth-tabs">
          <button
            className={!modoCadastro ? "active" : ""}
            onClick={() => setModoCadastro(false)}
          >
            Login
          </button>

          <button
            className={modoCadastro ? "active" : ""}
            onClick={() => setModoCadastro(true)}
          >
            Cadastro
          </button>
        </div>

        <form onSubmit={modoCadastro ? cadastrarUsuario : entrar}>
          {modoCadastro && (
            <input
              type="text"
              placeholder="Nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />
          )}

          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <button className="auth-button" type="submit">
            {modoCadastro ? "CADASTRAR" : "ENTRAR"}
          </button>
        </form>

        <div className="auth-info">
          <span>🏆 Ranking entre amigos</span>
          <span>⚽ Palpites da Copa</span>
          <span>🔥 Pontuação automática</span>
        </div>
      </section>
    </main>
  );
}

export default LoginCadastro;