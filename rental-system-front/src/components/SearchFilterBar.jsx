export default function SearchFilterBar({
  search,
  onSearchChange,
  sort,
  onSortChange,
  view,
  onViewChange,
  onClear,
}) {
  return (
    <div className="flex flex-col md:flex-row gap-4 md:items-center md:justify-between mb-6">
      <div className="flex gap-3 w-full md:w-2/3">
        <div className="relative flex-1">
          <input
            value={search}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Buscar por nome, documento ou telefone..."
            className="w-full border border-gray-200 rounded bg-white px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
          />
          <button
            onClick={onClear}
            className="absolute right-1.5 top-1/2 -translate-y-1/2 px-3 py-1 text-xs text-gray-500 rounded hover:bg-gray-100"
          >
            Limpar
          </button>
        </div>

        <select
          value={sort}
          onChange={(e) => onSortChange(e.target.value)}
          className="border border-gray-200 rounded bg-white px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
        >
          <option value="name,asc">Nome ↑</option>
          <option value="name,desc">Nome ↓</option>
          <option value="id,asc">ID ↑</option>
          <option value="id,desc">ID ↓</option>
        </select>
      </div>

      {view && onViewChange && (
        <div className="flex items-center gap-3">
          <div className="text-sm text-gray-500">Visualizar:</div>
          <div className="inline-flex items-center rounded bg-gray-100 p-0.5 text-sm">
            <button
              onClick={() => onViewChange("cards")}
              className={`px-3 py-1.5 rounded transition ${view === "cards" ? "bg-white text-gray-900" : "text-gray-500"}`}
            >
              Cards
            </button>
            <button
              onClick={() => onViewChange("table")}
              className={`px-3 py-1.5 rounded transition ${view === "table" ? "bg-white text-gray-900" : "text-gray-500"}`}
            >
              Tabela
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
