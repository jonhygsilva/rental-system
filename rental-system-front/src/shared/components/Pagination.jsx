export default function Pagination({ page, size, totalElements, onChange }) {
  const totalPages = Math.ceil(totalElements / size);
  const pages = [];

  const start = Math.max(0, page - 2);
  const end = Math.min(totalPages - 1, page + 2);

  for (let i = start; i <= end; i++) pages.push(i);

  if (totalPages === 0) return null;

  return (
    <nav className="flex items-center justify-center gap-2 mt-6" aria-label="Pagination">
      <button
        onClick={() => onChange(0)}
        disabled={page === 0}
        className="flex items-center gap-2 px-2.5 py-1.5 rounded border border-gray-200 bg-white text-sm text-gray-600 hover:border-gray-300 hover:shadow-sm disabled:opacity-50 disabled:bg-gray-50"
        aria-label="first page"
      >
        «
      </button>

      <button
        onClick={() => onChange(Math.max(0, page - 1))}
        disabled={page === 0}
        className="px-2.5 py-1.5 rounded border border-gray-200 bg-white text-sm text-gray-600 hover:border-gray-300 hover:shadow-sm disabled:opacity-50 disabled:bg-gray-50"
        aria-label="previous page"
      >
        ‹
      </button>

      {pages.map(p => (
        <button
          key={p}
          onClick={() => onChange(p)}
          aria-current={p === page ? "page" : undefined}
          className={`px-3.5 py-1.5 rounded text-sm font-semibold border transition ${p === page ? "bg-gray-900 text-white border-gray-900" : "bg-white text-gray-700 border-gray-200 hover:border-gray-300"}`}
        >
          {p + 1}
        </button>
      ))}

      <button
        onClick={() => onChange(Math.min(totalPages - 1, page + 1))}
        disabled={page + 1 >= totalPages}
        className="px-2.5 py-1.5 rounded border border-gray-200 bg-white text-sm text-gray-600 hover:border-gray-300 hover:shadow-sm disabled:opacity-50 disabled:bg-gray-50"
        aria-label="next page"
      >
        ›
      </button>

      <button
        onClick={() => onChange(totalPages - 1)}
        disabled={page + 1 >= totalPages}
        className="px-2.5 py-1.5 rounded border border-gray-200 bg-white text-sm text-gray-600 hover:border-gray-300 hover:shadow-sm disabled:opacity-50 disabled:bg-gray-50"
        aria-label="last page"
      >
        »
      </button>

    </nav>
  );
}
