import { customers as _customers, equipments as _equipments, rentals as _rentals } from './mockData';

// In-memory copies
const customers = [..._customers];
const equipments = [..._equipments];
let rentals = [..._rentals];

const nextId = (arr) => (arr.length ? Math.max(...arr.map((i) => i.id)) + 1 : 1);

export const getCustomers = (params) => {
  // simulate async axios-like response
  return Promise.resolve({ data: customers });
};

export const getCustomer = (id) => Promise.resolve({ data: customers.find((c) => c.id === Number(id)) });

export const getEquipments = () => Promise.resolve({ data: equipments });
export const getEquipment = (id) => Promise.resolve({ data: equipments.find((e) => e.id === Number(id)) });

export const getRentals = () => Promise.resolve({ data: rentals });

export const getRental = (id) => {
  const rental = rentals.find((r) => r.id === Number(id));
  if (!rental) return Promise.resolve({ data: null });
  
  // Populate address from customer
  const customer = customers.find((c) => c.id === rental.customerId);
  const address = customer?.addresses?.find((a) => a.id === rental.addressId);
  
  return Promise.resolve({ 
    data: {
      ...rental,
      address: address || null
    }
  });
};

export const createRental = (payload) => {
  const id = nextId(rentals);
  const customer = customers.find((c) => Number(c.id) === Number(payload.customerId));
  const equipment = equipments.find((e) => Number(e.id) === Number(payload.equipmentId));
  const item = {
    id,
    customerId: Number(payload.customerId),
    customerName: customer?.name || (payload.customerName || ''),
    addressId: Number(payload.addressId),
    equipmentId: Number(payload.equipmentId),
    equipmentName: equipment?.name || (payload.equipmentName || ''),
    startDate: payload.startDate,
    endDate: payload.endDate,
    total: payload.total ?? 0,
    status: payload.status || 'PLANNED',
  };
  rentals = [item, ...rentals];
  return Promise.resolve({ data: item });
};

export const updateRental = (id, payload) => {
  rentals = rentals.map((r) => (r.id === Number(id) ? { ...r, ...payload, id: Number(id) } : r));
  return Promise.resolve({ data: rentals.find((r) => r.id === Number(id)) });
};

export const deleteRental = (id) => {
  rentals = rentals.filter((r) => r.id !== Number(id));
  return Promise.resolve({ data: true });
};

export const markAsReturned = (id) => {
  rentals = rentals.map((r) => (r.id === Number(id) ? { ...r, status: 'RETURNED' } : r));
  return Promise.resolve({ data: rentals.find((r) => r.id === Number(id)) });
};

export default {
  getCustomers,
  getCustomer,
  getEquipments,
  getEquipment,
  getRentals,
  getRental,
  createRental,
  updateRental,
  deleteRental,
  markAsReturned,
};
