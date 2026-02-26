import axios from "axios";

const api = axios.create();

// Interceptor: injeta o token em todas as requisições
api.interceptors.request.use((config) => {
  const stored = localStorage.getItem("user");
  if (stored) {
    try {
      const { token } = JSON.parse(stored);
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    } catch {
      // ignora erro de parse
    }
  }
  return config;
});

// Interceptor: se receber 401, redireciona pro login (exceto rotas de auth)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const url = error.config?.url || "";
    const status = error.response?.status;
    const isAuthRoute = url.includes("/login") || url.includes("/register");

    console.log("API ERROR:", status, url, error.response?.data);

    if (!isAuthRoute && (status === 401 || status === 403)) {
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
