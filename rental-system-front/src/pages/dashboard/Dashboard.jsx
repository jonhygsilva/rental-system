import { useEffect, useMemo, useState } from "react";
import {
  Users,
  Package,
  AlertTriangle,
  DollarSign,
  TrendingUp,
  Calendar,
  ArrowUpRight,
  ArrowDownRight,
  Loader2,
} from "lucide-react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  AreaChart,
  Area,
} from "recharts";
import { motion } from "framer-motion";
import {
  getDashboardSummary,
  getEquipmentStats,
  getMonthlyRevenue,
  getRecentRentals,
  getTopCustomers,
} from "./api/DashboardApi";

// ─── Helpers ────────────────────────────────────────────────
const PERIOD_OPTIONS = [
  { label: "Últimos 3 meses", months: 3 },
  { label: "Últimos 6 meses", months: 6 },
  { label: "Últimos 12 meses", months: 12 },
  { label: "Este ano", months: 0 }, // special: jan → hoje
];

function buildDateRange(months) {
  const end = new Date();
  let start;
  if (months === 0) {
    start = new Date(end.getFullYear(), 0, 1); // 1 jan
  } else {
    start = new Date(end);
    start.setMonth(start.getMonth() - months);
  }
  return {
    startDate: start.toISOString().slice(0, 10),
    endDate: end.toISOString().slice(0, 10),
  };
}

function formatCurrency(v) {
  return (v ?? 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

const MONTH_LABELS = ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"];

function formatMonthLabel(ym) {
  // ym = "2026-01"
  const [, m] = (ym || "").split("-");
  return MONTH_LABELS[parseInt(m, 10) - 1] || ym;
}

const STATUS_COLORS = {
  DISPONIVEL: "#10b981",
  EM_USO: "#3b82f6",
  MANUTENCAO: "#f59e0b",
};
const STATUS_LABELS = {
  DISPONIVEL: "Disponível",
  EM_USO: "Em Uso",
  MANUTENCAO: "Manutenção",
};

const RENTAL_STATUS_BADGE = {
  ACTIVE: { bg: "bg-green-50 text-green-700", label: "Em Andamento" },
  RETURNED: { bg: "bg-gray-100 text-gray-600", label: "Devolvido" },
  CANCELLED: { bg: "bg-orange-50 text-orange-700", label: "Cancelado" },
  OVERDUE: { bg: "bg-red-50 text-red-700", label: "Atrasado" },
};

// ─── Sub-componentes ────────────────────────────────────────
function StatCard({ title, value, icon, color, subtitle }) {
  return (
    <div className="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center gap-4">
      <div className={`p-3 rounded-lg ${color}`}>{icon}</div>
      <div className="min-w-0">
        <p className="text-sm text-gray-500 font-medium truncate">{title}</p>
        <p className="text-2xl font-bold text-gray-800">{value}</p>
        {subtitle && <p className="text-xs text-gray-400 mt-0.5">{subtitle}</p>}
      </div>
    </div>
  );
}

function PeriodSelector({ selected, onChange }) {
  return (
    <div className="flex items-center gap-2 flex-wrap">
      <Calendar size={16} className="text-gray-400" />
      {PERIOD_OPTIONS.map((opt) => (
        <button
          key={opt.months}
          onClick={() => onChange(opt.months)}
          className={`px-3 py-1 rounded-full text-sm transition ${
            selected === opt.months
              ? "bg-gray-900 text-white"
              : "bg-gray-100 text-gray-600 hover:bg-gray-200"
          }`}
        >
          {opt.label}
        </button>
      ))}
    </div>
  );
}

function CustomTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null;
  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-lg px-3 py-2 text-sm">
      <p className="font-medium text-gray-700">{label}</p>
      {payload.map((p, i) => (
        <p key={i} className="text-gray-500">
          {p.name}: {p.name === "revenue" || p.name === "Receita" ? formatCurrency(p.value) : p.value}
        </p>
      ))}
    </div>
  );
}

// ─── Dashboard principal ────────────────────────────────────
export default function Dashboard() {
  const [period, setPeriod] = useState(6);
  const [loading, setLoading] = useState(true);

  const [summary, setSummary] = useState(null);
  const [equipStats, setEquipStats] = useState([]);
  const [monthlyData, setMonthlyData] = useState([]);
  const [recentRentals, setRecentRentals] = useState([]);
  const [topCustomers, setTopCustomers] = useState([]);

  const dateRange = useMemo(() => buildDateRange(period), [period]);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      getDashboardSummary(dateRange).catch(() => ({ data: null })),
      getEquipmentStats().catch(() => ({ data: null })),
      getMonthlyRevenue(dateRange).catch(() => ({ data: null })),
      getRecentRentals({ size: 6 }).catch(() => ({ data: null })),
      getTopCustomers({ size: 5 }).catch(() => ({ data: null })),
    ]).then(([sumRes, eqRes, moRes, reRes, tcRes]) => {
      setSummary(sumRes.data);
      setEquipStats(eqRes.data);
      setMonthlyData(moRes.data);
      setRecentRentals(reRes.data);
      setTopCustomers(tcRes.data);
      setLoading(false);
    });
  }, [dateRange]);

  // ── Dados derivados ──
  const totalRevenue = summary?.totalRevenue ?? 0;
  const totalRentals = summary?.totalRentals ?? 0;
  const activeRentals = summary?.activeRentals ?? 0;
  const overdueRentals = summary?.overdueRentals ?? 0;
  const totalCustomers = summary?.totalCustomers ?? 0;
  const totalEquipments = summary?.totalEquipments ?? 0;

  const monthlyChartData = useMemo(
    () =>
      (monthlyData || []).map((m) => ({
        month: formatMonthLabel(m.month),
        Receita: m.revenue ?? 0,
        Aluguéis: m.count ?? 0,
      })),
    [monthlyData]
  );

  const avgMonthly = useMemo(() => {
    if (!monthlyChartData.length) return 0;
    return monthlyChartData.reduce((s, m) => s + m.Receita, 0) / monthlyChartData.length;
  }, [monthlyChartData]);

  const pieData = useMemo(() => {
    if (!equipStats) return [];
    const stats = equipStats.equipmentsByStatus || equipStats;
    if (Array.isArray(stats)) return stats;
    return Object.entries(stats).map(([key, value]) => ({
      name: STATUS_LABELS[key] || key,
      value,
      color: STATUS_COLORS[key] || "#94a3b8",
    }));
  }, [equipStats]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[500px]">
        <Loader2 size={32} className="animate-spin text-gray-400" />
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.25 }}
      className="p-6 bg-gray-50 min-h-screen"
    >
      {/* Header + Period selector */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
          <p className="text-gray-500">Visão geral do sistema de locações.</p>
        </div>
        <PeriodSelector selected={period} onChange={setPeriod} />
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard
          title="Receita no Período"
          value={formatCurrency(totalRevenue)}
          icon={<DollarSign className="text-green-600" />}
          color="bg-green-100"
          subtitle={`Média mensal: ${formatCurrency(avgMonthly)}`}
        />
        <StatCard
          title="Aluguéis Ativos"
          value={activeRentals}
          icon={<TrendingUp className="text-blue-600" />}
          color="bg-blue-100"
          subtitle={`${totalRentals} no período`}
        />
        <StatCard
          title="Aluguéis Vencidos"
          value={overdueRentals}
          icon={<AlertTriangle className="text-red-600" />}
          color="bg-red-100"
        />
        <StatCard
          title="Clientes"
          value={totalCustomers}
          icon={<Users className="text-purple-600" />}
          color="bg-purple-100"
          subtitle={`${totalEquipments} equipamentos`}
        />
      </div>

      {/* Charts row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        {/* Revenue Area Chart (2/3) */}
        <div className="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-700 flex items-center gap-2">
              <TrendingUp size={20} /> Receita Mensal
            </h3>
            <span className="text-xs text-gray-400">
              {dateRange.startDate} → {dateRange.endDate}
            </span>
          </div>
          <div className="h-72">
            {monthlyChartData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={monthlyChartData}>
                  <defs>
                    <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.15} />
                      <stop offset="95%" stopColor="#3b82f6" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                  <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `R$${(v / 1000).toFixed(0)}k`} />
                  <Tooltip content={<CustomTooltip />} />
                  <Area
                    type="monotone"
                    dataKey="Receita"
                    stroke="#3b82f6"
                    strokeWidth={2}
                    fill="url(#colorRevenue)"
                  />
                </AreaChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex items-center justify-center h-full text-gray-400 text-sm">
                Sem dados de receita no período.
              </div>
            )}
          </div>
        </div>

        {/* Equipment status Pie (1/3) */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-lg font-semibold mb-4 text-gray-700 flex items-center gap-2">
            <Package size={20} /> Status dos Equipamentos
          </h3>
          <div className="h-52">
            {pieData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={pieData} innerRadius={55} outerRadius={80} paddingAngle={4} dataKey="value">
                    {pieData.map((entry, i) => (
                      <Cell key={i} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex items-center justify-center h-full text-gray-400 text-sm">
                Sem dados.
              </div>
            )}
          </div>
          <div className="flex justify-center gap-4 mt-2 flex-wrap">
            {pieData.map((s) => (
              <span key={s.name} className="text-xs text-gray-500 flex items-center gap-1">
                <span className="w-3 h-3 rounded-full inline-block" style={{ backgroundColor: s.color }} />
                {s.name} ({s.value})
              </span>
            ))}
          </div>
        </div>
      </div>

      {/* Rental count bar chart + stats row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-lg font-semibold mb-4 text-gray-700">Aluguéis por Mês</h3>
          <div className="h-64">
            {monthlyChartData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={monthlyChartData}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                  <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                  <Tooltip />
                  <Bar dataKey="Aluguéis" fill="#6366f1" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex items-center justify-center h-full text-gray-400 text-sm">
                Sem dados no período.
              </div>
            )}
          </div>
        </div>

        {/* Quick stats */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex flex-col gap-4">
          <h3 className="text-lg font-semibold text-gray-700">Resumo Rápido</h3>
          <div className="flex-1 flex flex-col justify-center gap-5">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-blue-50 rounded-lg">
                <ArrowUpRight size={18} className="text-blue-600" />
              </div>
              <div>
                <p className="text-xs text-gray-400">Média mensal</p>
                <p className="text-lg font-bold text-gray-800">{formatCurrency(avgMonthly)}</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-green-50 rounded-lg">
                <DollarSign size={18} className="text-green-600" />
              </div>
              <div>
                <p className="text-xs text-gray-400">Total no período</p>
                <p className="text-lg font-bold text-gray-800">{formatCurrency(totalRevenue)}</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-purple-50 rounded-lg">
                <Package size={18} className="text-purple-600" />
              </div>
              <div>
                <p className="text-xs text-gray-400">Total de equipamentos</p>
                <p className="text-lg font-bold text-gray-800">{totalEquipments}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom: Recent Rentals + Top Customers */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Rentals */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-lg font-semibold mb-4 text-gray-700">Aluguéis Recentes</h3>
          <div className="space-y-3">
            {(recentRentals || []).length > 0 ? (
              recentRentals.map((r) => {
                console.log(r);
                const badge = r.endDate > new Date() ? RENTAL_STATUS_BADGE.OVERDUE : RENTAL_STATUS_BADGE[r.status];
                return (
                  <div key={r.id} className="flex items-center justify-between border-b border-gray-50 pb-3 last:border-0">
                    <div className="min-w-0">
                      <p className="text-sm font-medium text-gray-800 truncate">
                        {r.customerName || `Cliente #${r.customerId}`}
                      </p>
                      <p className="text-xs text-gray-400 truncate">
                        {r.equipmentName || `Equip #${r.equipmentId}`} · {r.startDate} → {r.endDate}
                      </p>
                    </div>
                    <div className="text-right flex-shrink-0 ml-3">
                      <p className="text-sm font-semibold text-gray-800">{formatCurrency(r.total)}</p>
                      <span className={`text-xs px-2 py-0.5 rounded-full ${badge.bg}`}>{badge.label}</span>
                    </div>
                  </div>
                );
              })
            ) : (
              <p className="text-sm text-gray-400 text-center py-4">Nenhum aluguel recente.</p>
            )}
          </div>
        </div>

        {/* Top Customers */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-lg font-semibold mb-4 text-gray-700">Top Clientes</h3>
          <table className="w-full text-left">
            <thead>
              <tr className="text-gray-400 border-b text-xs uppercase tracking-wide">
                <th className="pb-3 font-medium">Cliente</th>
                <th className="pb-3 font-medium text-center">Aluguéis</th>
                <th className="pb-3 font-medium text-right">Investido</th>
              </tr>
            </thead>
            <tbody>
              {(topCustomers || []).length > 0 ? (
                topCustomers.map((c, i) => (
                  <tr key={i} className="border-b last:border-0 hover:bg-gray-50 transition">
                    <td className="py-3 text-sm font-medium text-gray-700">{c.name}</td>
                    <td className="py-3 text-sm text-center text-gray-600">{c.totalRentals}</td>
                    <td className="py-3 text-sm text-right font-semibold text-gray-900">
                      {formatCurrency(c.totalSpent)}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={3} className="py-6 text-center text-sm text-gray-400">
                    Sem dados de clientes.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </motion.div>
  );
}