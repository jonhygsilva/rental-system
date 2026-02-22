export default function ToggleView({ view, onChange }) {
  return (
    <div className="inline-flex items-center rounded-lg bg-gray-100 p-1">
      <button
        onClick={() => onChange("cards")}
        className={`px-3 py-1 rounded-lg text-sm ${view === "cards" ? "bg-white shadow" : "text-gray-600"}`}
      >
        Cards
      </button>
      <button
        onClick={() => onChange("table")}
        className={`px-3 py-1 rounded-lg text-sm ${view === "table" ? "bg-white shadow" : "text-gray-600"}`}
      >
        Table
      </button>
    </div>
  );
}
