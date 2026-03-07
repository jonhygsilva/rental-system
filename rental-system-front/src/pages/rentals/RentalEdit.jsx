import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';

// APIs
import { getRental, updateRental } from './api/RentalsApi';
import { getCustomers } from '../customers/api/CustomersApi';
import { getEquipments } from '../equipments/api/EquipmentsApi';

// Componentes
import Breadcrumbs from '../../shared/components/Breadcrumbs';
import Card from '../../components/Card';
import PaginatedSelect from '../../components/PaginatedSelect';
import { extractApiError } from '../../utils/apiErrors';

export default function RentalEdit() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [customerAddresses, setCustomerAddresses] = useState([]);
 
  const [form, setForm] = useState({ 
    customerId: '', 
    addressId: '', 
    equipmentId: '', 
    startDate: '', 
    endDate: '', 
    total: '',
  });

  useEffect(() => {
    if (!id) return;

    getRental(id)
      .then((res) => {
        const r = res.data || {};

        const mappedData = {
          customerId: r.customer?.id ? String(r.customer.id) : '',
          addressId: r.address?.id ? String(r.address.id) : '',
          equipmentId: r.equipment?.id ? String(r.equipment.id) : '',
          startDate: r.startDate || '', 
          endDate: r.endDate || '',
          total: r.total || ''
        };

        setForm(mappedData);

        // Load addresses of the existing customer
        if (mappedData.customerId) {
          import('../customers/api/CustomersApi').then(({ getCustomer }) => {
            getCustomer(mappedData.customerId).then((cRes) => {
              setCustomerAddresses(cRes.data?.addresses || []);
            });
          });
        }

        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro ao carregar dados:", err);
        setLoading(false);
      });
  }, [id]);

  const handleCustomerChange = (customerId) => {
    setForm((p) => ({ ...p, customerId, addressId: '' }));
    setCustomerAddresses([]);
    
    if (customerId) {
      import('../customers/api/CustomersApi').then(({ getCustomer }) => {
        getCustomer(customerId).then((res) => {
          setCustomerAddresses(res.data?.addresses || []);
        });
      });
    }
  };

  const handle = (e) => setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const submit = (ev) => {
    ev.preventDefault();
    // Enviamos o formulário (IDs planos) para a API de atualização
    updateRental(id, form)
      .then(() => navigate('/rentals'))
      .catch((err) => {
        alert(extractApiError(err, 'Erro ao atualizar o aluguel. Tente novamente.'));
      });
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <p className="text-gray-500 animate-pulse">Carregando dados do aluguel...</p>
      </div>
    );
  }

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <Breadcrumbs items={[{ label: 'Aluguéis', to: '/rentals' }, { label: 'Editar' }]} />
      <h1 className="text-2xl font-semibold mb-6 text-gray-800">Editar Aluguel</h1>

      <form onSubmit={submit} className="space-y-4">
        {/* CLIENTE E ENDEREÇO */}
        <Card title="Informações do Cliente">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cliente *</label>
              <PaginatedSelect
                fetchFn={getCustomers}
                value={form.customerId}
                onChange={handleCustomerChange}
                renderOption={(c) => c.name}
                placeholder="-- Selecione um cliente --"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Endereço de Entrega *</label>
              <select 
                name="addressId" 
                value={form.addressId} 
                onChange={handle} 
                disabled={!form.customerId}
                className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none transition-all disabled:bg-gray-50"
              >
                <option value="">-- Selecione um endereço --</option>
                {customerAddresses.map((addr) => (
                  <option key={addr.id} value={String(addr.id)}>
                    {addr.street}, {addr.number} - {addr.city}/{addr.state}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </Card>

        {/* EQUIPAMENTO E VALOR */}
        <Card title="Informações do Equipamento">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Equipamento *</label>
              <PaginatedSelect
                fetchFn={getEquipments}
                value={form.equipmentId}
                onChange={(eqId) => setForm((p) => ({ ...p, equipmentId: eqId }))}
                renderOption={(eq) => `${eq.name} (${eq.type})`}
                placeholder="-- Selecione um equipamento --"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Valor Total (R$)</label>
              <input 
                name="total" 
                type="number" 
                step="0.01"
                value={form.total} 
                onChange={handle} 
                className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none transition-all" 
                placeholder="0.00" 
              />
            </div>
          </div>
        </Card>

        {/* DATAS */}
        <Card title="Período do Aluguel">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Inicial *</label>
              <input 
                name="startDate" 
                type="date" 
                value={form.startDate} 
                onChange={handle} 
                className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none transition-all" 
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Final *</label>
              <input 
                name="endDate" 
                type="date" 
                value={form.endDate} 
                onChange={handle} 
                className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none transition-all" 
              />
            </div>
          </div>
        </Card>

        {/* AÇÕES */}
        <div className="flex justify-end gap-3 pt-6">
          <button 
            type="button" 
            onClick={() => navigate('/rentals')} 
            className="px-6 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-600 hover:bg-gray-50 transition shadow-sm"
          >
            Cancelar
          </button>
          <button 
            type="submit" 
            className="px-6 py-2 bg-blue-600 text-white rounded-md text-sm font-medium hover:bg-blue-700 transition shadow-md"
          >
            Salvar Alterações
          </button>
        </div>
      </form>
    </motion.div>
  );
}