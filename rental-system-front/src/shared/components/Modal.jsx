export default function Modal({ open, onClose, onConfirm, title, message }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg w-96 shadow-lg">
        <h2 className="text-lg font-bold mb-3">{title}</h2>
        <p className="mb-6">{message}</p>

        <div className="flex justify-end gap-3">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 rounded bg-gray-900 text-white text-sm hover:bg-gray-800 transition"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  );
}
