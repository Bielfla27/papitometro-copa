import { useCallback, useEffect, useState } from "react";
import axios from "axios";
import "./Salas.css";

export default function Salas({ usuarioLogado, onSelecionarSala }) {
  const [salas, setSalas] = useState([]);
  const [nomeSala, setNomeSala] = useState("");
  const [codigoSala, setCodigoSala] = useState("");
  const [carregando, setCarregando] = useState(true);
  const [mensagemErro, setMensagemErro] = useState("");

  const usuarioId = usuarioLogado.id;

  const buscarMinhasSalas = useCallback(async () => {
    try {
      setCarregando(true);
      setMensagemErro("");

      const response = await axios.get(
        `http://localhost:8080/salas/minhas/${usuarioId}`
      );

      setSalas(response.data);
    } catch (error) {
      console.error("Erro ao buscar salas:", error);
      setMensagemErro("Não foi possível carregar suas salas.");
    } finally {
      setCarregando(false);
    }
  }, [usuarioId]);

  useEffect(() => {
    Promise.resolve().then(buscarMinhasSalas);
  }, [buscarMinhasSalas]);

  async function criarSala(event) {
    event.preventDefault();

    if (!nomeSala.trim()) return;

    const response = await axios.post("http://localhost:8080/salas", {
      nome: nomeSala.trim(),
      usuarioId: Number(usuarioId),
    });

    localStorage.setItem("salaId", response.data.id);
    localStorage.setItem("salaNome", response.data.nome);

    buscarMinhasSalas();
    setNomeSala("");
  }

  async function entrarSala(event) {
    event.preventDefault();

    if (!codigoSala.trim()) return;

    const response = await axios.post("http://localhost:8080/salas/entrar", {
      codigo: codigoSala.trim(),
      usuarioId: Number(usuarioId),
    });

    localStorage.setItem("salaId", response.data.id);
    localStorage.setItem("salaNome", response.data.nome);

    buscarMinhasSalas();
    setCodigoSala("");
  }

  function selecionarSala(sala) {
    onSelecionarSala(sala);
  }

  return (
    <div className="salas-container">
      <main className="salas-content">
        <header className="salas-header">
          <div>
            <span className="salas-eyebrow">Papitômetro 2026</span>
            <h1>Minhas salas</h1>
          </div>

          <div className="salas-user">
            <span>{usuarioLogado.nome}</span>
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
            />

            <button type="submit" disabled={!nomeSala.trim()}>
              Criar sala
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
            />

            <button type="submit" disabled={!codigoSala.trim()}>
              Entrar
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
              <button
                key={sala.id}
                className="card-sala"
                onClick={() => selecionarSala(sala)}
                type="button"
              >
                <span className="card-sala-tag">Sala</span>
                <h3>{sala.nome}</h3>
                <p>
                  Código: <strong>{sala.codigo}</strong>
                </p>
              </button>
            ))
          )}
        </section>
      </main>
    </div>
  );
}
