import { useEffect, useState } from 'react';
import { getRental, updateRental } from '../../mocks/mockApi';
import { useParams, useNavigate } from 'react-router-dom';
import Breadcrumbs from '../../shared/components/Breadcrumbs';
import Card from '../../components/Card';
import { motion } from 'framer-motion';
import { AlertTriangle, CheckCircle, Clock, Save } from 'lucide-react';
import { getRentalStatus, getStatusBadge, getDaysInfo } from '../../utils/rentalHelpers';

export default function RentalView() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [rental, setRental] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState('');
  const [saving, setSaving] = useState(false);

  const load = () => {
    getRental(id).then((res) => {
      setRental(res.data);
      setSelectedStatus(res.data?.status || 'ACTIVE');
    });
  };

  useEffect(() => {
    load();
  }, [id]);

  const handleStatusChange = () => {
    setSaving(true);
    updateRental(id, { status: selectedStatus }).then(() => {
      setSaving(false);
      load();
    });
  };

  if (!rental) return <div className="p-6">Carregando...</div>;

  const rentalStatus = getRentalStatus(rental.endDate, rental.status);
  const badge = getStatusBadge(rentalStatus);
  const daysInfo = getDaysInfo(rental.endDate, rental.status);
  const hasChanges = selectedStatus !== rental.status;

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <Breadcrumbs items={[{ label: 'Aluguéis', to: '/rentals' }, { label: `Aluguel ${rental.id}` }]} />

      <h1 className="text-2xl font-semibold mb-4">Aluguel #{rental.id}</h1>

      {/* Overdue Alert */}
      {rentalStatus === 'overdue' && rental.status !== 'RETURNED' && rental.status !== 'CANCELLED' && (
        <div className="mb-4 p-4 bg-red-50 border border-red-200 rounded flex items-start gap-3">
          <AlertTriangle className="text-red-600 flex-shrink-0 mt-0.5" size={20} />
          <div className="flex-1">
            <h3 className="text-sm font-semibold text-red-800">Aluguel Vencido</h3>
            <p className="text-sm text-red-700 mt-1">
              Este aluguel está vencido. O equipamento deveria ter sido devolvido em {rental.endDate}. {daysInfo}.
            </p>
          </div>
        </div>
      )}

      {/* Near Expiry Alert */}
      {rentalStatus === 'near-expiry' && rental.status !== 'RETURNED' && rental.status !== 'CANCELLED' && (
        <div className="mb-4 p-4 bg-yellow-50 border border-yellow-200 rounded flex items-start gap-3">
          <Clock className="text-yellow-600 flex-shrink-0 mt-0.5" size={20} />
          <div className="flex-1">
            <h3 className="text-sm font-semibold text-yellow-800">Próximo do Vencimento</h3>
            <p className="text-sm text-yellow-700 mt-1">
              Este aluguel está próximo do vencimento. {daysInfo}. Entre em contato com o cliente para agendar a devolução.
            </p>
          </div>
        </div>
      )}

      {/* Returned Success */}
      {rental.status === 'RETURNED' && (
        <div className="mb-4 p-4 bg-green-50 border border-green-200 rounded flex items-start gap-3">
          <CheckCircle className="text-green-600 flex-shrink-0 mt-0.5" size={20} />
          <div className="flex-1">
            <h3 className="text-sm font-semibold text-green-800">Equipamento Devolvido</h3>
            <p className="text-sm text-green-700 mt-1">
              Este aluguel foi finalizado e o equipamento foi devolvido.
            </p>
          </div>
        </div>
      )}

      {/* Cancelled */}
      {rental.status === 'CANCELLED' && (
        <div className="mb-4 p-4 bg-gray-50 border border-gray-200 rounded flex items-start gap-3">
          <AlertTriangle className="text-gray-600 flex-shrink-0 mt-0.5" size={20} />
          <div className="flex-1">
            <h3 className="text-sm font-semibold text-gray-800">Aluguel Cancelado</h3>
            <p className="text-sm text-gray-700 mt-1">
              Este aluguel foi cancelado.
            </p>
          </div>
        </div>
      )}

      <Card>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <p className="text-xs text-gray-500">Cliente</p>
            <p className="text-sm font-medium">{rental.customerName || rental.customer?.name}</p>

            <p className="text-xs text-gray-500 mt-3">Endereço de Entrega</p>
            {rental.address ? (
              <div className="text-sm">
                <p className="font-medium">{rental.address.street}, {rental.address.number}</p>
                {rental.address.complement && <p className="text-gray-600">{rental.address.complement}</p>}
                <p className="text-gray-600">{rental.address.neighborhood}</p>
                <p className="text-gray-600">{rental.address.city} - {rental.address.state}</p>
                <p className="text-gray-600">CEP: {rental.address.zipCode}</p>
              </div>
            ) : (
              <p className="text-sm text-gray-500">Endereço não informado</p>
            )}

            <p className="text-xs text-gray-500 mt-3">Equipamento</p>
            <p className="text-sm font-medium">{rental.equipmentName || rental.equipment?.name}</p>

            <p className="text-xs text-gray-500 mt-3">Período</p>
            <p className="text-sm">{rental.startDate} → {rental.endDate}</p>
            <p className="text-xs text-gray-500 mt-1">{daysInfo}</p>
          </div>

          <div>
            <p className="text-xs text-gray-500">Total</p>
            <p className="text-sm font-medium">R$ {rental.total}</p>

            <p className="text-xs text-gray-500 mt-3">Status Atual</p>
            <span className={`inline-flex items-center px-2 py-1 rounded text-xs font-medium border ${badge.bg} ${badge.text} ${badge.border}`}>
              {badge.label}
            </span>
          </div>
        </div>
      </Card>

      {/* Status Management Section */}
      <Card title="Gerenciar Status do Aluguel">
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Alterar Status
            </label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="w-full md:w-80 border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
            >
              <option value="ACTIVE">Ativo</option>
              <option value="RETURNED">Devolvido</option>
              <option value="CANCELLED">Cancelado</option>
              <option value="PENDING">Pendente</option>
            </select>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={handleStatusChange}
              disabled={!hasChanges || saving}
              className={`inline-flex items-center gap-2 px-4 py-2 rounded text-sm transition ${
                hasChanges && !saving
                  ? 'bg-gray-900 text-white hover:bg-gray-800'
                  : 'bg-gray-200 text-gray-400 cursor-not-allowed'
              }`}
            >
              <Save size={16} />
              {saving ? 'Salvando...' : 'Salvar Alteração'}
            </button>
            
            {hasChanges && (
              <p className="text-xs text-gray-500">
                Você tem alterações não salvas
              </p>
            )}
          </div>

          {/* Status descriptions */}
          <div className="mt-4 p-3 bg-gray-50 rounded border border-gray-200">
            <p className="text-xs font-semibold text-gray-700 mb-2">Descrição dos Status:</p>
            <ul className="text-xs text-gray-600 space-y-1">
              <li><strong>Ativo:</strong> O aluguel está em andamento e o equipamento está com o cliente</li>
              <li><strong>Devolvido:</strong> O equipamento foi devolvido pelo cliente</li>
              <li><strong>Cancelado:</strong> O aluguel foi cancelado antes ou durante a execução</li>
              <li><strong>Pendente:</strong> O aluguel foi criado mas ainda não começou</li>
            </ul>
          </div>
        </div>
      </Card>

      <div className="flex justify-end gap-2 mt-4">
        <button onClick={() => navigate('/rentals')} className="px-4 py-2 bg-white border border-gray-200 rounded text-sm hover:bg-gray-50 transition">
          Voltar
        </button>
      </div>
    </motion.div>
  );
}
