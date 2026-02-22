export const customers = [
  { 
    id: 1, 
    name: 'João Silva', 
    document: '123.456.789-00', 
    phone: '(11) 99999-0000',
    addresses: [
      { id: 1, street: 'Rua das Flores', number: '123', neighborhood: 'Centro', city: 'São Paulo', state: 'SP', zipCode: '01234-567', complement: 'Apto 45' },
      { id: 2, street: 'Av. Paulista', number: '1000', neighborhood: 'Bela Vista', city: 'São Paulo', state: 'SP', zipCode: '01310-100', complement: '' },
    ]
  },
  { 
    id: 2, 
    name: 'Mariana Costa', 
    document: '987.654.321-00', 
    phone: '(21) 98888-1111',
    addresses: [
      { id: 3, street: 'Rua Copacabana', number: '456', neighborhood: 'Copacabana', city: 'Rio de Janeiro', state: 'RJ', zipCode: '22070-011', complement: 'Casa' },
    ]
  },
  { 
    id: 3, 
    name: 'Carlos Pereira', 
    document: '111.222.333-44', 
    phone: '(31) 97777-2222',
    addresses: [
      { id: 4, street: 'Av. Afonso Pena', number: '789', neighborhood: 'Centro', city: 'Belo Horizonte', state: 'MG', zipCode: '30130-001', complement: 'Sala 10' },
      { id: 5, street: 'Rua da Bahia', number: '321', neighborhood: 'Savassi', city: 'Belo Horizonte', state: 'MG', zipCode: '30160-011', complement: '' },
    ]
  },
];

export const equipments = [
  { id: 1, name: 'Gerador X200', type: 'Gerador', status: 'DISPONIVEL', dailyRate: 150, latitude: -23.55052, longitude: -46.633308 },
  { id: 2, name: 'Cortador Pro', type: 'Cortador', status: 'EM_USO', dailyRate: 80, latitude: -22.906847, longitude: -43.172896 },
  { id: 3, name: 'Andaime Forte', type: 'Andaime', status: 'DISPONIVEL', dailyRate: 40, latitude: -23.682160, longitude: -46.595868 },
];

export const rentals = [
  // Overdue (ended 2025-12-10, today is 2025-12-19)
  { id: 1, customerId: 1, customerName: 'João Silva', addressId: 1, equipmentId: 2, equipmentName: 'Cortador Pro', startDate: '2025-12-01', endDate: '2025-12-10', total: 800, status: 'ACTIVE' },
  // Near expiry (ends 2025-12-21, 2 days from now)
  { id: 2, customerId: 3, customerName: 'Carlos Pereira', addressId: 4, equipmentId: 1, equipmentName: 'Gerador X200', startDate: '2025-12-15', endDate: '2025-12-21', total: 900, status: 'ACTIVE' },
  // Active (ends 2025-12-30)
  { id: 3, customerId: 2, customerName: 'Mariana Costa', addressId: 3, equipmentId: 3, equipmentName: 'Andaime Forte', startDate: '2025-12-18', endDate: '2025-12-30', total: 480, status: 'ACTIVE' },
  // Returned (already marked as returned)
  { id: 4, customerId: 1, customerName: 'João Silva', addressId: 2, equipmentId: 1, equipmentName: 'Gerador X200', startDate: '2025-11-20', endDate: '2025-11-25', total: 750, status: 'RETURNED' },
];
 