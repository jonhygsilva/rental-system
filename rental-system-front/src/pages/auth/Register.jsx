import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "./api/UsersApi";
import { extractApiError } from "../../utils/apiErrors";

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({ name: "", email: "", password: "", confirmPassword: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handle = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const submit = (e) => {
    e.preventDefault();
    setError("");

    if (!form.name.trim() || !form.email.trim() || !form.password.trim()) {
      setError("Preencha todos os campos.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    if (form.password.length < 6) {
      setError("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    setLoading(true);
    registerUser({ name: form.name, email: form.email, password: form.password })
      .then(() => {
        navigate("/login");
      })
      .catch((err) => {
        setError(extractApiError(err, "Erro ao criar conta. Tente novamente."));
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
          <h1 className="text-xl font-semibold text-gray-900">Criar Conta</h1>
          <p className="text-sm text-gray-500 mt-1">Preencha os dados para se cadastrar</p>
        </div>

        <form onSubmit={submit} className="bg-white border border-gray-200 rounded p-6 space-y-4">
          {error && (
            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded px-3 py-2">
              {error}
            </div>
          )}

          <div>
            <label className="block text-sm text-gray-600 mb-1">Nome</label>
            <input
              name="name"
              type="text"
              placeholder="Seu nome"
              value={form.name}
              onChange={handle}
              className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
              autoFocus
            />
          </div>

          <div>
            <label className="block text-sm text-gray-600 mb-1">E-mail</label>
            <input
              name="email"
              type="email"
              placeholder="seu@email.com"
              value={form.email}
              onChange={handle}
              className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
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

          <div>
            <label className="block text-sm text-gray-600 mb-1">Confirmar Senha</label>
            <input
              name="confirmPassword"
              type="password"
              placeholder="••••••••"
              value={form.confirmPassword}
              onChange={handle}
              className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition disabled:opacity-50"
          >
            {loading ? "Criando..." : "Criar Conta"}
          </button>

          <p className="text-center text-sm text-gray-500">
            Já tem conta?{" "}
            <Link to="/login" className="text-gray-900 font-medium hover:underline">
              Faça login
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
