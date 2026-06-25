import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "https://papitometro-copa.onrender.com",
});

api.interceptors.request.use((config) => {
  const usuarioLogado = localStorage.getItem("usuarioLogado");

  if (usuarioLogado) {
    const usuario = JSON.parse(usuarioLogado);

    if (usuario.token) {
      config.headers.Authorization = `Bearer ${usuario.token}`;
    }
  }

  return config;
});

export default api;
