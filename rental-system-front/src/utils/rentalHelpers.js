// Rental status helper functions

/**
 * Calculate rental status based on end date and current date
 * @param {string} endDate - End date in YYYY-MM-DD format
 * @param {string} status - Current rental status
 * @returns {string} - 'overdue' | 'near-expiry' | 'active' | 'returned' | 'cancelled' | 'pending'
 */
export const getRentalStatus = (endDate, status) => {
  if (status === 'RETURNED') return 'returned';
  if (status === 'CANCELLED') return 'cancelled';
  if (status === 'PENDING') return 'pending';

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  const end = new Date(endDate);
  end.setHours(0, 0, 0, 0);
  
  const diffTime = end - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  if (diffDays < 0) return 'overdue';
  if (diffDays <= 3) return 'near-expiry'; // 3 days or less
  return 'active';
};

/**
 * Get visual styling for rental status
 */
export const getStatusBadge = (rentalStatus) => {
  const badges = {
    overdue: {
      bg: 'bg-red-100',
      text: 'text-red-800',
      border: 'border-red-200',
      label: 'Vencido',
    },
    'near-expiry': {
      bg: 'bg-yellow-100',
      text: 'text-yellow-800',
      border: 'border-yellow-200',
      label: 'Próximo do Vencimento',
    },
    active: {
      bg: 'bg-green-100',
      text: 'text-green-800',
      border: 'border-green-200',
      label: 'Ativo',
    },
    returned: {
      bg: 'bg-gray-100',
      text: 'text-gray-800',
      border: 'border-gray-200',
      label: 'Devolvido',
    },
    cancelled: {
      bg: 'bg-gray-100',
      text: 'text-gray-600',
      border: 'border-gray-300',
      label: 'Cancelado',
    },
    pending: {
      bg: 'bg-blue-100',
      text: 'text-blue-800',
      border: 'border-blue-200',
      label: 'Pendente',
    },
  };
  
  return badges[rentalStatus] || badges.active;
};

/**
 * Get row styling for rental table
 */
export const getRowStyle = (rentalStatus) => {
  const styles = {
    overdue: 'bg-red-50 hover:bg-red-100',
    'near-expiry': 'bg-yellow-50 hover:bg-yellow-100',
    active: 'hover:bg-gray-50',
    returned: 'bg-gray-50 hover:bg-gray-100 opacity-75',
    cancelled: 'bg-gray-50 hover:bg-gray-100 opacity-60',
    pending: 'bg-blue-50 hover:bg-blue-100',
  };
  
  return styles[rentalStatus] || styles.active;
};

/**
 * Calculate days until/overdue
 */
export const getDaysInfo = (endDate, status) => {
  if (status === 'RETURNED') return 'Devolvido';
  if (status === 'CANCELLED') return 'Cancelado';
  if (status === 'PENDING') return 'Pendente';
  
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  const end = new Date(endDate);
  end.setHours(0, 0, 0, 0);
  
  const diffTime = end - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  if (diffDays < 0) return `Vencido há ${Math.abs(diffDays)} dia(s)`;
  if (diffDays === 0) return 'Vence hoje';
  if (diffDays === 1) return 'Vence amanhã';
  return `Vence em ${diffDays} dias`;
};
