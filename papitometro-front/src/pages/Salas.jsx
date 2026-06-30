import { useEffect, useState } from "react";
import api from "../services/api";
import "./Salas.css";

function getMensagemErro(error, fallback) {
  return (
    error.response?.data?.message ||
    error.response?.data?.error ||
    error.response?.data ||
    fallback
  );
}

export default function Salas({ usuarioLogado, onSelecionarSala, onLogout }) {
  const [salas, setSalas] = useState([]);
  const [nomeSala, setNomeSala] = useState("");
  const [codigoSala, setCodigoSala] = useState("");
  const [carregando, setCarregando] = useState(true);
  const [criando, setCriando] = useState(false);
  const [entrando, setEntrando] = useState(false);
  const [mensagemErro, setMensagemErro] = useState("");
  const [codigoCopiadoId, setCodigoCopiadoId] = useState(null);

  const usuarioId = usuarioLogado.id;

  async function buscarMinhasSalas() {
    setCarregando(true);
    setMensagemErro("");

    try {
      const response = await api.get(`/salas/minhas/${usuarioId}`);
      setSalas(response.data);
    } catch (error) {
      console.error("Erro ao buscar salas:", error);
      setMensagemErro(getMensagemErro(error, "Não foi possível carregar suas salas."));
      setSalas([]);
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    let ativo = true;

    async function carregar() {
      setCarregando(true);
      setMensagemErro("");

      try {
        const response = await api.get(`/salas/minhas/${usuarioId}`);

        if (ativo) {
          setSalas(response.data);
        }
      } catch (error) {
        console.error("Erro ao buscar salas:", error);

        if (ativo) {
          setMensagemErro(getMensagemErro(error, "Não foi possível carregar suas salas."));
          setSalas([]);
        }
      } finally {
        if (ativo) {
          setCarregando(false);
        }
      }
    }

    carregar();

    return () => {
      ativo = false;
    };
  }, [usuarioId]);

  async function criarSala(event) {
    event.preventDefault();

    const nome = nomeSala.trim();
    if (!nome || criando) return;

    setCriando(true);
    setMensagemErro("");

    try {
      const response = await api.post("/salas", {
        nome,
        usuarioId: Number(usuarioId),
      });

      localStorage.setItem("salaId", response.data.id);
      localStorage.setItem("salaNome", response.data.nome);

      setNomeSala("");
      await buscarMinhasSalas();
    } catch (error) {
      console.error("Erro ao criar sala:", error);
      setMensagemErro(getMensagemErro(error, "Não foi possível criar a sala."));
    } finally {
      setCriando(false);
    }
  }

  async function entrarSala(event) {
    event.preventDefault();

    const codigo = codigoSala.trim().toUpperCase();
    if (!codigo || entrando) return;

    setEntrando(true);
    setMensagemErro("");

    try {
      const response = await api.post("/salas/entrar", {
        codigo,
        usuarioId: Number(usuarioId),
      });

      localStorage.setItem("salaId", response.data.id);
      localStorage.setItem("salaNome", response.data.nome);

      setCodigoSala("");
      await buscarMinhasSalas();
    } catch (error) {
      console.error("Erro ao entrar na sala:", error);
      setMensagemErro(getMensagemErro(error, "Não foi possível entrar nessa sala."));
    } finally {
      setEntrando(false);
    }
  }

  async function copiarCodigoSala(sala) {
    try {
      if (navigator.clipboard?.writeText) {
        await navigator.clipboard.writeText(sala.codigo);
      } else {
        const campoTemporario = document.createElement("textarea");
        campoTemporario.value = sala.codigo;
        campoTemporario.setAttribute("readonly", "");
        campoTemporario.style.position = "absolute";
        campoTemporario.style.left = "-9999px";
        document.body.appendChild(campoTemporario);
        campoTemporario.select();
        document.execCommand("copy");
        document.body.removeChild(campoTemporario);
      }

      setCodigoCopiadoId(sala.id);
      window.setTimeout(() => {
        setCodigoCopiadoId((idAtual) => (idAtual === sala.id ? null : idAtual));
      }, 1800);
    } catch (error) {
      console.error("Erro ao copiar código da sala:", error);
      setMensagemErro("Não foi possível copiar o código da sala.");
    }
  }

  return (
    <div className="salas-container">
      <main className="salas-content">
        <header className="salas-header">
          <div>
            <span className="salas-eyebrow">Papitômetro 2026</span>
            <h1>Minhas salas</h1>
            <p className="salas-subtitulo">
              Crie uma sala ou entre com um código para jogar com amigos.
            </p>
          </div>

          <div className="salas-header-actions">
            <div className="salas-user">
              <span>{usuarioLogado.nome}</span>
            </div>

            <button className="salas-sair" type="button" onClick={onLogout}>
              Sair
            </button>
          </div>
        </header>

        <section className="salas-acoes" aria-label="Ações de sala">
          <form className="sala-box" onSubmit={criarSala}>
            <div className="sala-box-topo">
              <span className="sala-icone">+</span>
              <h2>Criar sala</h2>
            </div>

            <input
              type="text"
              placeholder="Nome da sala"
              value={nomeSala}
              onChange={(e) => setNomeSala(e.target.value)}
              disabled={criando}
            />

            <button type="submit" disabled={!nomeSala.trim() || criando}>
              {criando ? "Criando..." : "Criar sala"}
            </button>
          </form>

          <form className="sala-box" onSubmit={entrarSala}>
            <div className="sala-box-topo">
              <span className="sala-icone">#</span>
              <h2>Entrar com código</h2>
            </div>

            <input
              type="text"
              placeholder="Código da sala"
              value={codigoSala}
              onChange={(e) => setCodigoSala(e.target.value.toUpperCase())}
              disabled={entrando}
            />

            <button type="submit" disabled={!codigoSala.trim() || entrando}>
              {entrando ? "Entrando..." : "Entrar"}
            </button>
          </form>
        </section>

        <div className="salas-lista-topo">
          <h2 className="titulo-suas-salas">Suas salas</h2>
          <span>
            {salas.length} {salas.length === 1 ? "sala" : "salas"}
          </span>
        </div>

        {mensagemErro && <p className="salas-alerta">{mensagemErro}</p>}

        <section className="lista-salas" aria-label="Lista de salas">
          {carregando ? (
            <div className="salas-vazio">Carregando salas...</div>
          ) : salas.length === 0 ? (
            <div className="salas-vazio">Nenhuma sala encontrada.</div>
          ) : (
            salas.map((sala) => (
              <article key={sala.id} className="card-sala">
                <span className="card-sala-tag">Sala</span>
                <h3>{sala.nome}</h3>

                <div className="card-sala-codigo">
                  <span>Código</span>
                  <strong>{sala.codigo}</strong>
                </div>

                <div className="card-sala-acoes">
                  <button
                    className="card-sala-copiar"
                    type="button"
                    onClick={() => copiarCodigoSala(sala)}
                  >
                    {codigoCopiadoId === sala.id ? "Copiado!" : "Copiar código"}
                  </button>

                  <button
                    className="card-sala-entrar"
                    type="button"
                    onClick={() => onSelecionarSala(sala)}
                  >
                    Entrar na sala
                  </button>
                </div>
              </article>
            ))
          )}
        </section>
      </main>
    </div>
  );
}
