import { useEffect, useState, useRef } from 'react';
import { createRental, getCustomers, getEquipments } from '../../mocks/mockApi';
import { useNavigate } from 'react-router-dom';
import Breadcrumbs from '../../shared/components/Breadcrumbs';
import Card from '../../components/Card';
import { motion } from 'framer-motion';
import { extractApiError } from '../../utils/apiErrors';

export default function RentalForm() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ customerId: '', addressId: '', equipmentId: '', startDate: '', endDate: '', total: '' });
  const [errors, setErrors] = useState({});
  const customerRef = useRef(null);

  const validate = (f) => {
    const e = {};
    if (!f.customerId) e.customerId = 'Selecione um cliente.';
    if (!f.addressId) e.addressId = 'Selecione um endereço.';
    if (!f.equipmentId) e.equipmentId = 'Selecione um equipamento.';
    if (!f.startDate) e.startDate = 'Data inicial obrigatória.';
    if (!f.endDate) e.endDate = 'Data final obrigatória.';
    return e;
  };

  const [customers, setCustomers] = useState([]);
  const [equipments, setEquipments] = useState([]);
  const [customerAddresses, setCustomerAddresses] = useState([]);

  useEffect(() => {
    getCustomers().then((res) => setCustomers(res.data || []));
    getEquipments().then((res) => setEquipments(res.data || []));
  }, []);

  const handleCustomerChange = (e) => {
    const customerId = e.target.value;
    setForm((p) => ({ ...p, customerId, addressId: '' }));
    
    // Load customer addresses
    const selectedCustomer = customers.find(c => c.id === Number(customerId));
    setCustomerAddresses(selectedCustomer?.addresses || []);
  };

  const handle = (e) => setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const submit = (ev) => {
    ev.preventDefault();
    const e = validate(form);
    setErrors(e);
    if (Object.keys(e).length) { if (e.customerId && customerRef.current) customerRef.current.focus(); return; }

    createRental(form)
      .then(() => navigate('/rentals'))
      .catch((err) => {
        alert(extractApiError(err, 'Erro ao salvar o aluguel. Tente novamente.'));
      });
  };

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <Breadcrumbs items={[{ label: 'Aluguéis', to: '/rentals' }, { label: 'Novo Aluguel' }]} />
      <h1 className="text-2xl font-semibold mb-4">Novo Aluguel</h1>

      <form onSubmit={submit} className="space-y-4">
        <Card title="Informações do Cliente">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cliente *</label>
              <select ref={customerRef} name="customerId" value={form.customerId} onChange={handleCustomerChange} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400">
                <option value="">-- Selecione um cliente --</option>
                {customers.map((c) => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
              {errors.customerId && <p className="mt-1 text-xs text-red-600">{errors.customerId}</p>}
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
              {errors.addressId && <p className="mt-1 text-xs text-red-600">{errors.addressId}</p>}
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
              {errors.equipmentId && <p className="mt-1 text-xs text-red-600">{errors.equipmentId}</p>}
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
              {errors.startDate && <p className="mt-1 text-xs text-red-600">{errors.startDate}</p>}
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Final *</label>
              <input name="endDate" type="date" value={form.endDate} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
              {errors.endDate && <p className="mt-1 text-xs text-red-600">{errors.endDate}</p>}
            </div>
          </div>
        </Card>

        <div className="flex justify-end gap-2">
          <button type="button" onClick={() => navigate('/rentals')} className="px-4 py-2 border border-gray-200 rounded text-sm hover:bg-gray-50 transition">
            Cancelar
          </button>
          <button type="submit" className="px-4 py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">
            Salvar Aluguel
          </button>
        </div>
      </form>
    </motion.div>
  );
}
