import { Eye, Edit } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function EquipmentTable({ equipments }) {
  const navigate = useNavigate();

  const formatStatus = (status) => {
    switch (status) {
      case "DISPONIVEL": return "Disponível";
      case "EM_USO": return "Em Uso";
      case "MANUTENCAO": return "Manutenção";
      default: return status;
    }
  };

  return (
    <div className="overflow-auto bg-white shadow rounded-lg border border-gray-200">
      <table className="min-w-full divide-y divide-gray-100">
        <thead className="bg-gray-50">
          <tr>
            <th className="text-left p-3 text-sm font-semibold text-gray-600">Nome</th>
            <th className="text-left p-3 text-sm font-semibold text-gray-600">Tipo</th>
            <th className="text-left p-3 text-sm font-semibold text-gray-600">Status</th>
            <th className="text-left p-3 text-sm font-semibold text-gray-600">Preço/Dia</th>
            <th className="text-right p-3 text-sm font-semibold text-gray-600">Ações</th>
          </tr>
        </thead>

        <tbody className="bg-white divide-y divide-gray-100">
          {equipments.map((e) => (
            <tr key={e.id} className="hover:bg-gray-50 transition-colors">
              <td className="p-3 text-sm text-gray-700">{e.name}</td>
              <td className="p-3 text-sm text-gray-600">{e.type}</td>
              <td className="p-3 text-sm text-gray-600">{formatStatus(e.status)}</td>
              <td className="p-3 text-sm text-gray-600">R$ {e.dailyRate}</td>
              <td className="p-3 text-right">
                <button
                  onClick={() => navigate(`/equipments/${e.id}`)}
                  className="inline-flex items-center justify-center w-8 h-8 rounded-md hover:bg-gray-100 text-gray-600 mr-2"
                  aria-label={`Ver ${e.name}`}
                >
                  <Eye size={16} />
                </button>

                <button
                  onClick={() => navigate(`/equipments/${e.id}/edit`)}
                  className="inline-flex items-center justify-center w-8 h-8 rounded-md hover:bg-gray-100 text-gray-600"
                  aria-label={`Editar ${e.name}`}
                >
                  <Edit size={16} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
