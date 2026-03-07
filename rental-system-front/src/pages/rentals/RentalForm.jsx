import { useEffect, useState, useRef } from 'react';
import { createRental } from './api/RentalsApi';
import { getCustomers } from '../customers/api/CustomersApi';
import { getEquipments } from '../equipments/api/EquipmentsApi';
import { useNavigate } from 'react-router-dom';
import Breadcrumbs from '../../shared/components/Breadcrumbs';
import Card from '../../components/Card';
import PaginatedSelect from '../../components/PaginatedSelect';
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
    if (!f.startDate) e.startDate = 'Data de início obrigatória.';
    if (!f.endDate) e.endDate = 'Data de término obrigatória.';
    return e;
  };

  const [customerAddresses, setCustomerAddresses] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  const handleCustomerChange = (customerId) => {
    setForm((p) => ({ ...p, customerId, addressId: '' }));
    setCustomerAddresses([]);
    
    // Load customer details to get addresses
    if (customerId) {
      import('../customers/api/CustomersApi').then(({ getCustomer }) => {
        getCustomer(customerId).then((res) => {
          const c = res.data;
          setSelectedCustomer(c);
          setCustomerAddresses(c?.addresses || []);
        });
      });
    } else {
      setSelectedCustomer(null);
    }
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
              <PaginatedSelect
                fetchFn={getCustomers}
                value={form.customerId}
                onChange={handleCustomerChange}
                renderOption={(c) => c.name}
                placeholder="-- Selecione um cliente --"
              />
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
              <PaginatedSelect
                fetchFn={getEquipments}
                value={form.equipmentId}
                onChange={(eqId) => setForm((p) => ({ ...p, equipmentId: eqId }))}
                renderOption={(eq) => `${eq.name} — ${eq.type}`}
                placeholder="-- Selecione um equipamento --"
              />
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
