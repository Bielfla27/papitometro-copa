import { useEffect, useState } from "react";
import "./App.css";
import api from "./services/api";
import LoginCadastro from "./pages/LoginCadastro";

const datas = [];
const inicio = new Date("2026-06-11");
const fim = new Date("2026-07-19");

let dataAtual = new Date(inicio);

while (dataAtual <= fim) {
  const diaSemana = dataAtual.toLocaleDateString("pt-BR", {
    weekday: "short",
  });

  const mes = dataAtual.toLocaleDateString("pt-BR", {
    month: "short",
  });

  datas.push({
    diaSemana: diaSemana.replace(".", "").toUpperCase(),
    dia: dataAtual.getDate(),
    mes: mes.replace(".", "").toUpperCase(),
    dataCompleta: dataAtual.toISOString().split("T")[0],
  });

  dataAtual.setDate(dataAtual.getDate() + 1);
}

const rankingMock = [
  { nome: "Gabriel", pontos: 0 },
  { nome: "Pedro", pontos: 0 },
  { nome: "Theo", pontos: 0 },
  { nome: "Davi", pontos: 0 },
  { nome: "Hudson", pontos: 0 },
];

function getDataInicial() {
  const hoje = new Date().toISOString().split("T")[0];

  if (hoje < "2026-06-11") {
    return "2026-06-11";
  }

  if (hoje > "2026-07-19") {
    return "2026-07-19";
  }

  return hoje;
}

function App() {
  const [jogos, setJogos] = useState([]);
  const [palpites, setPalpites] = useState({});
  const [ranking] = useState(rankingMock);
  const [dataSelecionada, setDataSelecionada] = useState(getDataInicial());

  const [usuarioLogado, setUsuarioLogado] = useState(() => {
    const usuarioSalvo = localStorage.getItem("usuarioLogado");
    return usuarioSalvo ? JSON.parse(usuarioSalvo) : null;
  });

  useEffect(() => {
    api
      .get("/jogos")
      .then((response) => {
        setJogos(response.data);
      })
      .catch((error) => {
        console.error("Erro ao buscar jogos:", error);
      });
  }, []);

  function logout() {
    localStorage.removeItem("usuarioLogado");
    setUsuarioLogado(null);
  }

  function handlePalpite(jogoId, campo, valor) {
    setPalpites({
      ...palpites,
      [jogoId]: {
        ...palpites[jogoId],
        [campo]: valor,
      },
    });
  }

  function salvarPalpites() {
    console.log("Usuário logado:", usuarioLogado);
    console.log("Palpites:", palpites);
    alert("Por enquanto apenas exibindo no console.");
  }

  function jogoFinalizado(jogo) {
    return jogo.golsCasa !== null && jogo.golsFora !== null;
  }

  function getStatusClass(jogo) {
    const status = jogo.status?.toUpperCase();

    if (status === "FINISHED") return "finalizado";
    if (status === "TIMED") return "agendado";
    if (["LIVE", "IN_PLAY", "PAUSED"].includes(status)) return "ao-vivo";

    return "";
  }

  if (!usuarioLogado) {
    return <LoginCadastro onLogin={setUsuarioLogado} />;
  }

  const jogosFiltrados = jogos.filter((jogo) => {
    const dataDoJogo = jogo.dataHora.split("T")[0];
    return dataDoJogo === dataSelecionada;
  });

  return (
    <main className="container">
      <header className="topo-app">
        <h1>⚽ Papitômetro 2026</h1>

        <div className="usuario-info">
          <span>{usuarioLogado.nome}</span>

          <button className="logout-button" onClick={logout}>
            Sair
          </button>
        </div>
      </header>

      <div className="date-scroll">
        {datas.map((data) => (
          <button
            key={data.dataCompleta}
            onClick={() => setDataSelecionada(data.dataCompleta)}
            className={`date-card ${
              dataSelecionada === data.dataCompleta ? "active" : ""
            }`}
          >
            <span>{data.diaSemana}</span>
            <div className="date-number">{data.dia}</div>
            <small>{data.mes}</small>
          </button>
        ))}
      </div>

      <div className="content">
        <section className="games-area">
          <h2 className="titulo-data">
            {new Date(dataSelecionada + "T00:00:00")
              .toLocaleDateString("pt-BR", {
                weekday: "long",
                day: "2-digit",
                month: "long",
              })
              .toUpperCase()}
          </h2>

          {jogosFiltrados.length === 0 ? (
            <p>Nenhum jogo encontrado para essa data.</p>
          ) : (
            jogosFiltrados.map((jogo) => (
              <div
                className={`game-card ${getStatusClass(jogo)}`}
                key={jogo.id}
              >
                <div className="info-jogo">
                  <span className="time">
                    {new Date(jogo.dataHora).toLocaleTimeString("pt-BR", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </span>

                  {jogo.status?.toUpperCase() === "FINISHED" && (
                    <span className="badge-finalizado">FINALIZADO</span>
                  )}

                  {["LIVE", "IN_PLAY", "PAUSED"].includes(
                    jogo.status?.toUpperCase()
                  ) && <span className="badge-ao-vivo">AO VIVO</span>}

                  {jogo.status?.toUpperCase() === "TIMED" && (
                    <span className="badge-agendado">AGENDADO</span>
                  )}
                </div>

                <div className="team">
                  {jogo.escudoTimeCasa && (
                    <img
                      src={jogo.escudoTimeCasa}
                      alt={jogo.timeCasa || "Não definido"}
                    />
                  )}
                  <strong>{jogo.timeCasa || "Não definido"}</strong>
                </div>

                {jogoFinalizado(jogo) ? (
                  <>
                    <div className="placar-final">{jogo.golsCasa}</div>
                    <span className="versus">x</span>
                    <div className="placar-final">{jogo.golsFora}</div>
                  </>
                ) : (
                  <>
                    <input
                      type="number"
                      min="0"
                      value={palpites[jogo.id]?.golsCasa || ""}
                      onChange={(e) =>
                        handlePalpite(jogo.id, "golsCasa", e.target.value)
                      }
                    />

                    <span className="versus">x</span>

                    <input
                      type="number"
                      min="0"
                      value={palpites[jogo.id]?.golsFora || ""}
                      onChange={(e) =>
                        handlePalpite(jogo.id, "golsFora", e.target.value)
                      }
                    />
                  </>
                )}

                <div className="team">
                  {jogo.escudoTimeFora && (
                    <img
                      src={jogo.escudoTimeFora}
                      alt={jogo.timeFora || "Não definido"}
                    />
                  )}
                  <strong>{jogo.timeFora || "Não definido"}</strong>
                </div>
              </div>
            ))
          )}

          <button className="save-button" onClick={salvarPalpites}>
            SALVAR PALPITES
          </button>
        </section>

        <aside className="ranking">
          <h2>🏆 RANKING</h2>

          {ranking.map((item, index) => (
            <div className="ranking-row" key={item.nome}>
              <span>{index + 1}º</span>
              <strong>{item.nome}</strong>
              <b>{item.pontos}</b>
            </div>
          ))}
        </aside>
      </div>
    </main>
  );
}

export default App;