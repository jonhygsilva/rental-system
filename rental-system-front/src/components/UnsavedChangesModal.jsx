import { motion, AnimatePresence } from "framer-motion";

export default function UnsavedChangesModal({ show, onCancel, onConfirm }) {
  return (
    <AnimatePresence>
      {show && (
        <motion.div
          className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
          <motion.div
            className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md"
            initial={{ scale: 0.95, opacity: 0, y: 20 }}
            animate={{ scale: 1, opacity: 1, y: 0 }}
            exit={{ scale: 0.95, opacity: 0, y: 20 }}
            transition={{ duration: 0.25 }}
          >
            {/* Título */}
            <h2 className="text-2xl font-semibold text-gray-800 mb-3">
              Alterações não salvas
            </h2>

            {/* Mensagem */}
            <p className="text-gray-600 mb-6">
              Você fez alterações que ainda não foram salvas.  
              Deseja realmente sair da página?
            </p>

            {/* Botões */}
            <div className="flex justify-end gap-3">
              <button
                onClick={onCancel}
                className="px-4 py-2 rounded border border-gray-200 text-gray-700 hover:bg-gray-50 transition"
              >
                Continuar editando
              </button>

              <button
                onClick={onConfirm}
                className="px-4 py-2 bg-gray-900 text-white rounded hover:bg-gray-800 transition"
              >
                Sair sem salvar
              </button>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
