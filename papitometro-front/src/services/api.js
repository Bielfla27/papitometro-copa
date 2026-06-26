import axios from "axios";

const apiUrl =
  import.meta.env.VITE_API_URL ||
  (import.meta.env.PROD
    ? "https://papitometro-copa.onrender.com"
    : "http://localhost:8080");

const api = axios.create({
  baseURL: apiUrl,
  timeout: 15000,
});

api.interceptors.request.use((config) => {
  const usuarioLogado = localStorage.getItem("usuarioLogado");

  if (usuarioLogado) {
    try {
      const usuario = JSON.parse(usuarioLogado);

      if (usuario.token) {
        config.headers.Authorization = `Bearer ${usuario.token}`;
      }
    } catch {
      localStorage.removeItem("usuarioLogado");
    }
  }

  return config;
});

export default api;
