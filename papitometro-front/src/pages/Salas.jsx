import { useEffect, useState } from "react";
import axios from "axios";
import "./Salas.css";

export default function Salas({ usuarioLogado, onSelecionarSala }) {
  const [salas, setSalas] = useState([]);
  const [nomeSala, setNomeSala] = useState("");
  const [codigoSala, setCodigoSala] = useState("");

  const usuarioId = usuarioLogado.id;

  useEffect(() => {
    buscarMinhasSalas();
  }, []);

  async function buscarMinhasSalas() {
    const response = await axios.get(`http://localhost:8080/salas/minhas/${usuarioId}`);
    setSalas(response.data);
  }

  async function criarSala() {
    const response = await axios.post("http://localhost:8080/salas", {
      nome: nomeSala,
      usuarioId: Number(usuarioId)
    });

    localStorage.setItem("salaId", response.data.id);
    localStorage.setItem("salaNome", response.data.nome);

    buscarMinhasSalas();
    setNomeSala("");
  }

  async function entrarSala() {
    const response = await axios.post("http://localhost:8080/salas/entrar", {
      codigo: codigoSala,
      usuarioId: Number(usuarioId)
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
      <h1>🏆 Minhas Salas</h1>

      <div className="salas-acoes">
        <div>
          <h2>Criar sala</h2>
          <input
            type="text"
            placeholder="Nome da sala"
            value={nomeSala}
            onChange={(e) => setNomeSala(e.target.value)}
          />
          <button onClick={criarSala}>Criar</button>
        </div>

        <div>
          <h2>Entrar com código</h2>
          <input
            type="text"
            placeholder="Código da sala"
            value={codigoSala}
            onChange={(e) => setCodigoSala(e.target.value)}
          />
          <button onClick={entrarSala}>Entrar</button>
        </div>
      </div>

      <h2>Suas salas</h2>

      <div className="lista-salas">
        {salas.map((sala) => (
          <div key={sala.id} className="card-sala" onClick={() => selecionarSala(sala)}>
            <h3>{sala.nome}</h3>
            <p>Código: {sala.codigo}</p>
          </div>
        ))}
      </div>
    </div>
  );
}