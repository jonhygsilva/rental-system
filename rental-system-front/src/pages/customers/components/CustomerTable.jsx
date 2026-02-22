// CustomerTable.jsx

import { Eye } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function CustomerTable({ customers }) {
  const navigate = useNavigate();

  return (
    <div className="overflow-hidden bg-white border border-gray-200 rounded-lg">
      <table className="min-w-full divide-y divide-gray-100">
        <thead className="bg-gray-50/80">
          <tr>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Nome</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Documento</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Telefone</th>
            <th className="text-right px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Ações</th>
          </tr>
        </thead>

        <tbody className="divide-y divide-gray-100">
          {customers.map(c => (
            <tr key={c.id} className="hover:bg-gray-50/80 transition-colors">
              <td className="px-4 py-3 text-sm text-gray-800">{c.name}</td>
              <td className="px-4 py-3 text-sm text-gray-600">{c.document}</td>
              <td className="px-4 py-3 text-sm text-gray-600">{c.phone}</td>
              <td className="px-4 py-3 text-right">
                <button
                  onClick={() => navigate(`/customers/view/${c.id}`)}
                  className="inline-flex items-center justify-center w-9 h-9 border border-gray-200 rounded text-gray-500 hover:text-gray-900 hover:bg-gray-50 transition"
                  aria-label={`Ver ${c.name}`}
                >
                  <Eye size={16} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
