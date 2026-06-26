import { useEffect, useRef, useState } from "react";
import "./App.css";
import api from "./services/api";
import LoginCadastro from "./pages/LoginCadastro";
import Salas from "./pages/Salas";

function formatarDataLocal(data) {
  return [
    data.getFullYear(),
    String(data.getMonth() + 1).padStart(2, "0"),
    String(data.getDate()).padStart(2, "0"),
  ].join("-");
}

const datas = [];
const inicio = new Date(2026, 5, 11);
const fim = new Date(2026, 6, 19);

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
    dataCompleta: formatarDataLocal(dataAtual),
  });

  dataAtual.setDate(dataAtual.getDate() + 1);
}

function getDataInicial() {
  const hoje = formatarDataLocal(new Date());

  if (hoje < "2026-06-11") return "2026-06-11";
  if (hoje > "2026-07-19") return "2026-07-19";

  return hoje;
}



function App() {
  const [jogos, setJogos] = useState([]);
  const [palpites, setPalpites] = useState({});
  const [ranking, setRanking] = useState([]);
  const [dataSelecionada, setDataSelecionada] = useState(getDataInicial());
  const [palpitesPorJogo, setPalpitesPorJogo] = useState({});
  const [jogoAbertoId, setJogoAbertoId] = useState(null);
  const [menuAberto, setMenuAberto] = useState(false);
  const dataSelecionadaRef = useRef(null);
  const jogosRef = useRef(null);
  const rankingRef = useRef(null);

  const [salaId, setSalaId] = useState(() => {
  const salaSalva = localStorage.getItem("salaSelecionada");
  return salaSalva ? JSON.parse(salaSalva).id : null;
});

  const [salaSelecionada, setSalaSelecionada] = useState(() => {
    const salaSalva = localStorage.getItem("salaSelecionada");
    return salaSalva ? JSON.parse(salaSalva) : null;
  });

  const [usuarioLogado, setUsuarioLogado] = useState(() => {
    const usuarioSalvo = localStorage.getItem("usuarioLogado");
    return usuarioSalvo ? JSON.parse(usuarioSalvo) : null;
  });

  useEffect(() => {
      if (!salaId) return;

      api
        .get(`/palpites/ranking/sala/${salaId}`)
        .then((response) => {
          setRanking(response.data);
        })
        .catch((error) => {
          console.error("Erro ao buscar ranking:", error);
        });
    }, [salaId]);

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

  useEffect(() => {
  if (!usuarioLogado?.id || !salaId) return;

  api
    .get(`/palpites/usuario/${usuarioLogado.id}/sala/${salaId}`)
    .then((response) => {
      const palpitesMap = {};

      response.data.forEach((palpite) => {
        palpitesMap[palpite.jogoId] = {
          id: palpite.id,
          golsCasa: palpite.golsCasa,
          golsFora: palpite.golsFora,
          pontos: palpite.pontos,
        };
      });

      setPalpites(palpitesMap);
    })
    .catch((error) => {
      console.error("Erro ao buscar palpites:", error);
    });
}, [usuarioLogado, salaId]);

  useEffect(() => {
    dataSelecionadaRef.current?.scrollIntoView({
      behavior: "smooth",
      block: "nearest",
      inline: "center",
    });
  }, [dataSelecionada, salaSelecionada]);

  function logout() {
    localStorage.removeItem("usuarioLogado");
    localStorage.removeItem("salaSelecionada");
    localStorage.removeItem("salaId");
    localStorage.removeItem("salaNome");

    setUsuarioLogado(null);
    setSalaSelecionada(null);
    setSalaId(null);
  }

  function voltarParaSalas() {
    setMenuAberto(false);
    localStorage.removeItem("salaSelecionada");
    localStorage.removeItem("salaId");
    localStorage.removeItem("salaNome");

    setSalaSelecionada(null);
    setSalaId(null);
    setRanking([]);
    setPalpites({});
    setPalpitesPorJogo({});
    setJogoAbertoId(null);
  }

  function fecharMenu() {
    setMenuAberto(false);
  }

  function irParaJogos() {
    setMenuAberto(false);
    jogosRef.current?.scrollIntoView({ behavior: "smooth", block: "start" });
  }

  function irParaRanking() {
    setMenuAberto(false);
    rankingRef.current?.scrollIntoView({ behavior: "smooth", block: "start" });
  }

  function sairPeloMenu() {
    setMenuAberto(false);
    logout();
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

 async function salvarPalpites() {
      try {
        if (!usuarioLogado?.id) {
          alert("Usuário logado inválido.");
          return;
        }

        const palpitesParaSalvar = Object.entries(palpites)
          .filter(([jogoId, palpite]) => {
            const jogo = jogos.find((j) => j.id === Number(jogoId));

            if (!jogo) return false;

            const podePalpitar = jogoPodeReceberPalpite(jogo);

            const palpitePreenchido =
              palpite.golsCasa !== "" &&
              palpite.golsCasa !== undefined &&
              palpite.golsFora !== "" &&
              palpite.golsFora !== undefined;

            return podePalpitar && palpitePreenchido;
          })
          .map(([jogoId, palpite]) => ({
                id: palpite.id,
                usuarioId: usuarioLogado.id,
                jogoId: Number(jogoId),
                salaId: Number(salaId),
                golsCasa: Number(palpite.golsCasa),
                golsFora: Number(palpite.golsFora),
            }));

        if (palpitesParaSalvar.length === 0) {
          alert("Nenhum palpite válido para salvar. Só é possível palpitar em jogos agendados.");
          return;
        }

        const novosPalpites = { ...palpites };

        for (const palpite of palpitesParaSalvar) {
          let response;

          if (palpite.id) {
            response = await api.put(`/palpites/${palpite.id}`, palpite);
          } else {
            response = await api.post("/palpites", palpite);
          }

          novosPalpites[response.data.jogoId] = {
            id: response.data.id,
            golsCasa: response.data.golsCasa,
            golsFora: response.data.golsFora,
            pontos: response.data.pontos,
          };
        }

        setPalpites(novosPalpites);
        alert("Palpites salvos com sucesso!");
        const rankingResponse = await api.get(`/palpites/ranking/sala/${salaId}`);
        setRanking(rankingResponse.data);
      } catch (error) {
        console.error("Erro ao salvar palpites:", error);

        const mensagem =
          error.response?.data?.message ||
          error.response?.data?.error ||
          error.response?.data ||
          "Erro ao salvar palpites.";

        alert(mensagem);
      }
    }

   async function carregarPalpitesDoJogo(jogoId) {
        try {
          if (jogoAbertoId === jogoId) {
            setJogoAbertoId(null);
            return;
          }

          const response = await api.get(`/palpites/jogo/${jogoId}/sala/${salaId}`);

          setPalpitesPorJogo({
            ...palpitesPorJogo,
            [jogoId]: response.data,
          });

          setJogoAbertoId(jogoId);
        } catch (error) {
          console.error("Erro ao buscar palpites do jogo:", error);
          alert("Erro ao carregar palpites desse jogo.");
        }
      }

  function jogoFinalizado(jogo) {
    return jogo.golsCasa !== null && jogo.golsFora !== null;
  }

  function jogoJaComecou(jogo) {
  const agora = new Date();
  const inicioJogo = new Date(jogo.dataHora);
  return inicioJogo <= agora;
}

function getStatusVisual(jogo) {
  const status = jogo.status?.toUpperCase();

  if (status === "FINISHED") return "FINALIZADO";

  if (["LIVE", "IN_PLAY", "PAUSED"].includes(status)) return "AO_VIVO";

  if (status === "TIMED" && jogoJaComecou(jogo)) return "AO_VIVO";

  return "AGENDADO";
}

function placarCasa(jogo) {
  if (jogo.golsCasa !== null) return jogo.golsCasa;
  if (getStatusVisual(jogo) === "AO_VIVO") return 0;
  return "";
}

function placarFora(jogo) {
  if (jogo.golsFora !== null) return jogo.golsFora;
  if (getStatusVisual(jogo) === "AO_VIVO") return 0;
  return "";
}

function getStatusClass(jogo) {
  const statusVisual = getStatusVisual(jogo);

  if (statusVisual === "FINALIZADO") return "finalizado";
  if (statusVisual === "AO_VIVO") return "ao-vivo";
  if (statusVisual === "AGENDADO") return "agendado";

  return "";
}

function jogoPodeReceberPalpite(jogo) {
  return getStatusVisual(jogo) === "AGENDADO";
}

  if (!usuarioLogado) {
    return (
      <LoginCadastro
        onLogin={(usuario) => {
          localStorage.removeItem("salaSelecionada");
          localStorage.removeItem("salaId");
          localStorage.removeItem("salaNome");

          setSalaSelecionada(null);
          setSalaId(null);
          setUsuarioLogado(usuario);
        }}
      />
    );
  }

   if (!salaSelecionada) {
  return (
    <Salas
      usuarioLogado={usuarioLogado}
      onLogout={logout}
     onSelecionarSala={(sala) => {
        const salaFormatada = {
          ...sala,
          id: Number(sala.id),
        };

        localStorage.setItem("salaSelecionada", JSON.stringify(salaFormatada));
        localStorage.setItem("salaId", salaFormatada.id);
        localStorage.setItem("salaNome", salaFormatada.nome);

        setRanking([]);
        setPalpites({});
        setPalpitesPorJogo({});
        setJogoAbertoId(null);

        setSalaSelecionada(salaFormatada);
        setSalaId(salaFormatada.id);
      }}
    />
  );
}

  const jogosFiltrados = jogos
  .filter((jogo) => {
    const dataDoJogo = jogo.dataHora.split("T")[0];
    return dataDoJogo === dataSelecionada;
  })
  .sort((a, b) => {
    return new Date(a.dataHora) - new Date(b.dataHora);
  });

  return (
    <main className="container">
      <header className="topo-app">
        <div className="topo-identidade">
          <h1>{"\u26BD"} Papitômetro 2026</h1>
          <div className="topo-meta">
            <p>
              {"\u{1F3C6}"} <span>Sala:</span>{" "}
              <strong>{localStorage.getItem("salaNome")}</strong>
            </p>
            <p>
              {"\u{1F464}"} <strong>{usuarioLogado.nome}</strong>
            </p>
          </div>
        </div>

        <button
          className="menu-button"
          type="button"
          onClick={() => setMenuAberto(true)}
          aria-label="Abrir menu"
        >
          ☰
        </button>
      </header>

      {menuAberto && (
        <div className="menu-overlay" onClick={fecharMenu}>
          <aside
            className="menu-drawer"
            aria-label="Menu"
            onClick={(event) => event.stopPropagation()}
          >
            <div className="menu-topo">
              <h2>☰ Menu</h2>
              <button type="button" onClick={fecharMenu} aria-label="Fechar menu">
                ×
              </button>
            </div>

            <div className="menu-perfil">
              <strong>{"\u{1F464}"} {usuarioLogado.nome}</strong>
              <span>{usuarioLogado.email}</span>
            </div>

            <nav className="menu-nav">
              <button type="button" onClick={irParaJogos}>
                {"\u{1F3E0}"} Jogos
              </button>
              <button type="button" onClick={voltarParaSalas}>
                {"\u{1F3C6}"} Minhas Salas
              </button>
              <button type="button" onClick={irParaRanking}>
                {"\u{1F4CA}"} Ranking
              </button>
            </nav>

            <button className="menu-sair" type="button" onClick={sairPeloMenu}>
              {"\u{1F6AA}"} Sair
            </button>
          </aside>
        </div>
      )}

      <div className="date-scroll">
        {datas.map((data) => (
          <button
            key={data.dataCompleta}
            ref={dataSelecionada === data.dataCompleta ? dataSelecionadaRef : null}
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
        <section className="games-area" ref={jogosRef}>
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
                className={`game-card ${getStatusClass(jogo)} ${
                  palpites[jogo.id]?.id ? "palpitado" : ""
                }`}
                key={jogo.id}
              >
                <div className="info-jogo">
                  <span className="time">
                    {new Date(jogo.dataHora).toLocaleTimeString("pt-BR", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </span>

                  {palpites[jogo.id]?.id && (
                    <span className="badge-palpitado">PALPITE FEITO</span>
                  )}

                  {getStatusVisual(jogo) === "FINALIZADO" && (
                    <span className="badge-finalizado">FINALIZADO</span>
                  )}

                  {getStatusVisual(jogo) === "AO_VIVO" && (
                    <span className="badge-ao-vivo">AO VIVO</span>
                  )}

                  {getStatusVisual(jogo) === "AGENDADO" && (
                    <span className="badge-agendado">AGENDADO</span>
                  )}

                  <button
                    className={`icone-palpites ${
                      jogoAbertoId === jogo.id ? "ativo" : ""
                    }`}
                    onClick={() => carregarPalpitesDoJogo(jogo.id)}
                    title="Ver palpites"
                  >
                    {"\u{1F465}"}
                  </button>
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

                {jogoFinalizado(jogo) || getStatusVisual(jogo) === "AO_VIVO" ? (
                  <>
                <div className="placar-final">{placarCasa(jogo)}</div>
                <span className="versus">x</span>
                <div className="placar-final">{placarFora(jogo)}</div>
                  </>
                ) : (
                  <>
                  
                    <input
                      type="number"
                      min="0"
                      disabled={!jogoPodeReceberPalpite(jogo)}
                      value={palpites[jogo.id]?.golsCasa ?? ""}
                      onChange={(e) =>
                        handlePalpite(jogo.id, "golsCasa", e.target.value)
                      }
                    />

                    <span className="versus">x</span>

                    <input
                      type="number"
                      min="0"
                      disabled={!jogoPodeReceberPalpite(jogo)}
                      value={palpites[jogo.id]?.golsFora ?? ""}
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
                
                {jogoAbertoId === jogo.id && (
                  <div className="palpites-jogo-box">
                 

                    <div className="cabecalho-palpites">
                      <span>Usuário</span>
                      <span>Palpite</span>
                      <span>Pontos</span>
                    </div>
                    {palpitesPorJogo[jogo.id]?.length > 0 ? (
                      palpitesPorJogo[jogo.id].map((palpite) => (
                        <div className="palpite-amigo" key={palpite.usuarioId}>
                          <strong>{palpite.usuarioNome}</strong>

                          <span>
                            {palpite.golsCasa} x {palpite.golsFora}
                          </span>

                          <b>{palpite.pontos} pts</b>
                        </div>
                      ))
                    ) : (
                      <p>Nenhum palpite feito para esse jogo ainda.</p>
                    )}
                  </div>
                )}

              </div>
            ))
          )}

          <button className="save-button" onClick={salvarPalpites}>
            SALVAR PALPITES
          </button>
        </section>

        <aside className="ranking" ref={rankingRef}>
          <h2>RANKING</h2>

          {ranking.map((item, index) => (
            <div
              className={`ranking-row ${
                item.usuarioId === usuarioLogado.id ? "ranking-row-logado" : ""
              }`}
              key={item.usuarioId ?? item.nome}
            >
              <span className="ranking-posicao">{index + 1}º</span>

              <div className="ranking-participante">
                <strong>
                  {item.nome}
                </strong>

                <div className="ranking-metricas">
                  {item.placaresExatos ?? 0} exatos | {item.acertos ?? 0} acertos | {item.palpites ?? 0} palpites
                </div>
              </div>

              <div className="ranking-pontos">
                <b>{item.pontos ?? 0}</b>
                <span>pts</span>
              </div>
            </div>
          ))}
        </aside>
      </div>
    </main>
  );
}

export default App;


