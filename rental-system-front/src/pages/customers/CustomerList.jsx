import { useEffect, useState, useCallback } from "react";
import { getCustomers } from "./api/CustomersApi";
import CustomerTable from "./components/CustomerTable";
import Pagination from "../../shared/components/Pagination";
import SearchFilterBar from "../../components/SearchFilterBar";
import TableSkeleton from "../../components/TableSkeleton";
import { useNavigate } from "react-router-dom";
import { Plus } from "lucide-react";
import { motion } from "framer-motion";
import { useAuth } from "../../context/AuthContext";

export default function CustomerList() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [search, setSearch] = useState("");
  const [sort, setSort] = useState("name,asc");
  const [page, setPage] = useState(0);
  const [size] = useState(9);
  const [totalElements, setTotalElements] = useState(0);

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetch = useCallback(() => {
    setLoading(true);
    getCustomers({ userId: user.userId, page, size, sort, search })
      .then(res => {
        const data = res.data;
        setCustomers(data.content || data);
        setTotalElements(data.totalElements ?? data.length);
      })
      .finally(() => setLoading(false));
  }, [page, size, sort, search]);

  useEffect(() => fetch(), [fetch]);

  useEffect(() => {
    const t = setTimeout(() => setPage(0), 300);
    return () => clearTimeout(t);
  }, [search, sort]);

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.25 }}
      className="p-6"
    >
      {/* HEADER */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Clientes</h1>
          <p className="text-gray-500">
            Gerencie facilmente seus clientes com busca, filtros e paginação.
          </p>
        </div>

        <button
          onClick={() => navigate("/customers/new")}
          className="inline-flex items-center gap-2 px-3 py-1.5 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition"
        >
          <Plus className="w-4 h-4" />
          Novo Cliente
        </button>
      </div>

      {/* SEARCH + FILTERS */}
      <SearchFilterBar
        search={search}
        onSearchChange={(v) => { setSearch(v); setPage(0); }}
        sort={sort}
        onSortChange={(v) => { setSort(v); setPage(0); }}
        onClear={() => { setSearch(""); setSort("name,asc"); }}
      />

      {/* DATA RENDER */}
      {loading ? (
        <div className="mt-6">
          <TableSkeleton rows={6} />
        </div>
      ) : (
        <>
          <div className="mt-6">
            <CustomerTable customers={customers} />
          </div>

          <Pagination
            page={page}
            size={size}
            totalElements={totalElements}
            onChange={(p) => setPage(p)}
          />
        </>
      )}
    </motion.div>
  );
}
