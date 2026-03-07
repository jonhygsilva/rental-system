import { useEffect, useState, useRef, useCallback } from "react";
import { ChevronDown, Search, Loader2 } from "lucide-react";

/**
 * PaginatedSelect — dropdown com busca e scroll infinito.
 *
 * Props:
 *  - fetchFn({ page, size, search }) → Promise<{ data: { content, totalElements } | Array }>
 *  - value            — id selecionado (string)
 *  - onChange(value)   — callback ao selecionar
 *  - renderOption(item) — texto exibido por item  (default: item.name)
 *  - placeholder       — texto quando nada selecionado
 *  - disabled
 *  - pageSize          — itens por página (default 10)
 *  - className
 */
export default function PaginatedSelect({
  fetchFn,
  value,
  onChange,
  renderOption = (item) => item.name,
  placeholder = "-- Selecione --",
  disabled = false,
  pageSize = 10,
  className = "",
}) {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const listRef = useRef(null);
  const containerRef = useRef(null);
  const debounceRef = useRef(null);

  // Fecha ao clicar fora
  useEffect(() => {
    const handler = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  // Busca itens
  const load = useCallback(
    (pageNum, searchTerm, append = false) => {
      if (loading) return;
      setLoading(true);
      fetchFn({ page: pageNum, size: pageSize, search: searchTerm })
        .then((res) => {
          const data = res.data;
          // API pode retornar { content: [...], totalElements } ou array direto
          const list = data?.content ?? data ?? [];
          const total = data?.totalElements ?? list.length;

          setItems((prev) => (append ? [...prev, ...list] : list));
          setHasMore(append ? items.length + list.length < total : list.length < total);
          setPage(pageNum);
        })
        .catch(() => {
          if (!append) setItems([]);
        })
        .finally(() => setLoading(false));
    },
    [fetchFn, pageSize, loading, items.length]
  );

  // Quando abre ou busca muda → carrega página 0
  useEffect(() => {
    if (!open) return;
    clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => load(0, search, false), 250);
    return () => clearTimeout(debounceRef.current);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, search]);

  // Scroll infinito
  const handleScroll = () => {
    const el = listRef.current;
    if (!el || loading || !hasMore) return;
    if (el.scrollTop + el.clientHeight >= el.scrollHeight - 8) {
      load(page + 1, search, true);
    }
  };

  // Label do item selecionado
  const selectedLabel = items.find((i) => String(i.id) === String(value));

  return (
    <div ref={containerRef} className={`relative ${className}`}>
      {/* Botão que abre o dropdown */}
      <button
        type="button"
        disabled={disabled}
        onClick={() => setOpen((p) => !p)}
        className="w-full flex items-center justify-between border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400 bg-white disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <span className={value ? "text-gray-800" : "text-gray-400"}>
          {selectedLabel ? renderOption(selectedLabel) : placeholder}
        </span>
        <ChevronDown size={14} className="text-gray-400" />
      </button>

      {/* Dropdown */}
      {open && (
        <div className="absolute z-50 mt-1 w-full bg-white border border-gray-200 rounded shadow-lg">
          {/* Campo de busca */}
          <div className="flex items-center gap-2 px-3 py-2 border-b border-gray-100">
            <Search size={14} className="text-gray-400" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Buscar..."
              className="flex-1 text-sm outline-none bg-transparent"
              autoFocus
            />
          </div>

          {/* Lista com scroll */}
          <ul
            ref={listRef}
            onScroll={handleScroll}
            className="max-h-48 overflow-y-auto"
          >
            {items.map((item) => (
              <li
                key={item.id}
                onClick={() => {
                  onChange(String(item.id));
                  setOpen(false);
                }}
                className={`px-3 py-2 text-sm cursor-pointer hover:bg-gray-50 transition-colors ${
                  String(item.id) === String(value) ? "bg-gray-100 font-medium" : ""
                }`}
              >
                {renderOption(item)}
              </li>
            ))}

            {loading && (
              <li className="flex items-center justify-center py-3">
                <Loader2 size={16} className="animate-spin text-gray-400" />
              </li>
            )}

            {!loading && items.length === 0 && (
              <li className="px-3 py-3 text-sm text-gray-400 text-center">
                Nenhum resultado encontrado.
              </li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
}
