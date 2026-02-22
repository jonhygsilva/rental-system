import { Eye, Edit } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { getRentalStatus, getStatusBadge, getRowStyle, getDaysInfo } from '../utils/rentalHelpers';

export default function RentalTable({ rentals }) {
  const navigate = useNavigate();

  return (
    <div className="overflow-hidden bg-white border border-gray-200 rounded-lg">
      <table className="min-w-full divide-y divide-gray-100">
        <thead className="bg-gray-50/80">
          <tr>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Cliente</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Equipamento</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Período</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Status</th>
            <th className="text-left px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Total</th>
            <th className="text-right px-4 py-3 text-xs font-medium uppercase tracking-wide text-gray-500">Ações</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-100">
          {rentals.map((r) => {
            const rentalStatus = getRentalStatus(r.endDate, r.status);
            const badge = getStatusBadge(rentalStatus);
            const rowStyle = getRowStyle(rentalStatus);
            const daysInfo = getDaysInfo(r.endDate, r.status);
            
            return (
              <tr key={r.id} className={`${rowStyle} hover:bg-gray-50/80 transition-colors`}>
                <td className="px-4 py-3 text-sm text-gray-800">{r.customerName || r.customer?.name || '-'}</td>
                <td className="px-4 py-3 text-sm text-gray-600">{r.equipmentName || r.equipment?.name || '-'}</td>
                <td className="px-4 py-3 text-sm text-gray-600">
                  <div>{r.startDate} → {r.endDate}</div>
                  <div className="text-xs text-gray-500 mt-1">{daysInfo}</div>
                </td>
                <td className="px-4 py-3">
                  <span className={`inline-flex items-center px-2.5 py-1.5 rounded-full text-xs font-semibold border ${badge.bg} ${badge.text} ${badge.border}`}>
                    {badge.label}
                  </span>
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">R$ {r.total ?? '-'}</td>
                <td className="px-4 py-3 text-right">
                  <button
                    onClick={() => navigate(`/rentals/${r.id}`)}
                    className="inline-flex items-center justify-center w-9 h-9 border border-gray-200 rounded text-gray-500 hover:text-gray-900 hover:bg-gray-50 transition mr-2"
                    aria-label={`Ver aluguel ${r.id}`}
                  >
                    <Eye size={16} />
                  </button>
                  <button
                    onClick={() => navigate(`/rentals/${r.id}/edit`)}
                    className="inline-flex items-center justify-center w-9 h-9 border border-gray-200 rounded text-gray-500 hover:text-gray-900 hover:bg-gray-50 transition"
                    aria-label={`Editar aluguel ${r.id}`}
                  >
                    <Edit size={16} />
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
