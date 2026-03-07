import api from "../../../shared/api/axiosInstance";

/**
 * Busca resumo do dashboard.
 * O backend deve retornar algo como:
 * {
 *   totalRentals: number,
 *   activeRentals: number,
 *   overdueRentals: number,
 *   totalRevenue: number,
 *   totalCustomers: number,
 *   totalEquipments: number,
 *   equipmentsByStatus: { DISPONIVEL: n, EM_USO: n, MANUTENCAO: n },
 *   monthlyRevenue: [{ month: "2026-01", revenue: 1234.56, count: 5 }, ...],
 *   recentRentals: [ { id, customerName, equipmentName, startDate, endDate, total, status } ],
 *   topCustomers: [ { name, totalRentals, totalSpent } ],
 * }
 *
 * @param {string} startDate  ISO date (yyyy-MM-dd)
 * @param {string} endDate    ISO date (yyyy-MM-dd)
 */
export const getDashboardSummary = ({ startDate, endDate } = {}) =>
  api.get("/api/dashboard/summary", { params: { startDate, endDate } });

export const getEquipmentStats = () =>
  api.get("/api/dashboard/equipment-stats");

export const getMonthlyRevenue = ({ startDate, endDate } = {}) =>
  api.get("/api/dashboard/monthly-revenue", { params: { startDate, endDate } });

export const getRecentRentals = ({ size = 5 } = {}) =>
  api.get("/api/dashboard/recent-rentals", { params: { size } });

export const getTopCustomers = ({ size = 5 } = {}) =>
  api.get("/api/dashboard/top-customers", { params: { size } });
