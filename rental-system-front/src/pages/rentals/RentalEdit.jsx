import { useEffect, useState } from 'react';
import { getRental, updateRental, getCustomers, getEquipments } from '../../mocks/mockApi';
import { useNavigate, useParams } from 'react-router-dom';
import Breadcrumbs from '../../shared/components/Breadcrumbs';
import Card from '../../components/Card';
import { motion } from 'framer-motion';
import { extractApiError } from '../../utils/apiErrors';

export default function RentalEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ customerId: '', addressId: '', equipmentId: '', startDate: '', endDate: '', total: '' });
  const [customers, setCustomers] = useState([]);
  const [equipments, setEquipments] = useState([]);
  const [customerAddresses, setCustomerAddresses] = useState([]);

  useEffect(() => {
    Promise.all([getRental(id), getCustomers(), getEquipments()]).then(([rental, c, e]) => {
      const rentalData = rental.data || {};
      setForm(rentalData);
      
      // Load customer addresses
      const customer = c.data.find(cust => cust.id === rentalData.customerId);
      setCustomerAddresses(customer?.addresses || []);
      
      setCustomers(c.data);
      setEquipments(e.data);
    });
  }, [id]);

  const handleCustomerChange = (e) => {
    const customerId = Number(e.target.value);
    setForm((p) => ({ ...p, customerId, addressId: '' }));
    
    // Load customer addresses
    const selectedCustomer = customers.find(c => c.id === customerId);
    setCustomerAddresses(selectedCustomer?.addresses || []);
  };

  const handle = (e) => setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const submit = (ev) => {
    ev.preventDefault();
    updateRental(id, form)
      .then(() => navigate('/rentals'))
      .catch((err) => {
        alert(extractApiError(err, 'Erro ao atualizar o aluguel. Tente novamente.'));
      });
  };

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <Breadcrumbs items={[{ label: 'Aluguéis', to: '/rentals' }, { label: 'Editar' }]} />
      <h1 className="text-2xl font-semibold mb-4">Editar Aluguel</h1>

      <form onSubmit={submit} className="space-y-4">
        <Card title="Informações do Cliente">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cliente *</label>
              <select name="customerId" value={form.customerId} onChange={handleCustomerChange} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400">
                <option value="">-- Selecione um cliente --</option>
                {customers.map((c) => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Endereço de Entrega *</label>
              <select 
                name="addressId" 
                value={form.addressId} 
                onChange={handle} 
                className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                disabled={!form.customerId}
              >
                <option value="">-- Selecione um endereço --</option>
                {customerAddresses.map((addr) => (
                  <option key={addr.id} value={addr.id}>
                    {addr.street}, {addr.number} - {addr.neighborhood}, {addr.city}/{addr.state}
                  </option>
                ))}
              </select>
              {!form.customerId && <p className="mt-1 text-xs text-gray-500">Selecione um cliente primeiro</p>}
            </div>
          </div>
        </Card>

        <Card title="Informações do Equipamento">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Equipamento *</label>
              <select name="equipmentId" value={form.equipmentId} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400">
                <option value="">-- Selecione um equipamento --</option>
                {equipments.map((eq) => (
                  <option key={eq.id} value={eq.id}>{eq.name} — {eq.type}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Valor Total</label>
              <input name="total" type="number" value={form.total} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" placeholder="R$" />
            </div>
          </div>
        </Card>

        <Card title="Período do Aluguel">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Inicial *</label>
              <input name="startDate" type="date" value={form.startDate} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Final *</label>
              <input name="endDate" type="date" value={form.endDate} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
            </div>
          </div>
        </Card>

        <div className="flex justify-end gap-2">
          <button type="button" onClick={() => navigate('/rentals')} className="px-4 py-2 border border-gray-200 rounded text-sm hover:bg-gray-50 transition">
            Cancelar
          </button>
          <button type="submit" className="px-4 py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">
            Atualizar Aluguel
          </button>
        </div>
      </form>
    </motion.div>
  );
}
