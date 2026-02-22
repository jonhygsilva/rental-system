export default function DeleteModal({ show, title, onCancel, onConfirm }) {
  if (!show) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40">
      <div className="bg-white p-6 rounded-lg shadow-lg">
        <h2 className="text-xl font-bold">{title}</h2>

        <p className="mt-2 mb-4 text-gray-600">Tem certeza que deseja excluir?</p>

        <div className="flex justify-end gap-3">
          <button className="px-4 py-2 bg-gray-300 rounded" onClick={onCancel}>
            Cancelar
          </button>
          <button className="px-4 py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition" onClick={onConfirm}>
            Excluir
          </button>
        </div>
      </div>
    </div>
  );
}
