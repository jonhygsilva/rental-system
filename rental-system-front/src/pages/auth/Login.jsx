import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "./api/UsersApi";
import { useAuth } from "../../context/AuthContext";
import { extractApiError } from "../../utils/apiErrors";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handle = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const submit = (e) => {
    e.preventDefault();
    setError("");

    if (!form.email.trim() || !form.password.trim()) {
      setError("Preencha e-mail e senha.");
      return;
    }

    setLoading(true);
    loginUser(form.email, form.password)
      .then((res) => {
        login(res.data);
        navigate("/customers");
      })
      .catch((err) => {
        console.log("LOGIN ERROR:", err);
        console.log("Response status:", err?.response?.status);
        console.log("Response data:", err?.response?.data);
        console.log("Error message:", err?.message);
        setError(extractApiError(err, "E-mail ou senha inválidos."));
      })
      .finally(() => setLoading(false));
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center h-10 w-10 rounded bg-gray-900 text-white text-sm font-semibold mb-3">
            RS
          </div>
          <h1 className="text-xl font-semibold text-gray-900">Rental System</h1>
          <p className="text-sm text-gray-500 mt-1">Faça login para continuar</p>
        </div>

        <form onSubmit={submit} className="bg-white border border-gray-200 rounded p-6 space-y-4">
          {error && (
            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded px-3 py-2">
              {error}
            </div>
          )}

          <div>
            <label className="block text-sm text-gray-600 mb-1">E-mail</label>
            <input
              name="email"
              type="email"
              placeholder="seu@email.com"
              value={form.email}
              onChange={handle}
              className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
              autoFocus
            />
          </div>

          <div>
            <label className="block text-sm text-gray-600 mb-1">Senha</label>
            <input
              name="password"
              type="password"
              placeholder="••••••••"
              value={form.password}
              onChange={handle}
              className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition disabled:opacity-50"
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>

          <p className="text-center text-sm text-gray-500">
            Não tem conta?{" "}
            <Link to="/register" className="text-gray-900 font-medium hover:underline">
              Cadastre-se
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
