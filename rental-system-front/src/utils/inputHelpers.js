export const onlyDigits = (value = "") => value.replace(/\D/g, "");

export function formatPhone(value = "") {
  const d = onlyDigits(value).slice(0, 11);
  if (d.length <= 2) return d;
  if (d.length <= 6) return `(${d.slice(0,2)}) ${d.slice(2)}`;
  if (d.length <= 10) return `(${d.slice(0,2)}) ${d.slice(2,7)}-${d.slice(7)}`;
  return `(${d.slice(0,2)}) ${d.slice(2,7)}-${d.slice(7,11)}`;
}

export function formatCEP(value = "") {
  const d = onlyDigits(value).slice(0, 8);
  if (d.length <= 5) return d;
  return `${d.slice(0,5)}-${d.slice(5)}`;
}

export function formatDocument(value = "") {
  const d = onlyDigits(value);
  // CPF 11 digits, CNPJ 14 digits
  if (d.length <= 11) {
    const p = d.slice(0,11);
    if (p.length <= 3) return p;
    if (p.length <= 6) return `${p.slice(0,3)}.${p.slice(3)}`;
    if (p.length <= 9) return `${p.slice(0,3)}.${p.slice(3,6)}.${p.slice(6)}`;
    return `${p.slice(0,3)}.${p.slice(3,6)}.${p.slice(6,9)}-${p.slice(9)}`;
  }

  const p = d.slice(0,14);
  if (p.length <= 2) return p;
  if (p.length <= 5) return `${p.slice(0,2)}.${p.slice(2)}`;
  if (p.length <= 8) return `${p.slice(0,2)}.${p.slice(2,5)}.${p.slice(5)}`;
  if (p.length <= 12) return `${p.slice(0,2)}.${p.slice(2,5)}.${p.slice(5,8)}/${p.slice(8)}`;
  return `${p.slice(0,2)}.${p.slice(2,5)}.${p.slice(5,8)}/${p.slice(8,12)}-${p.slice(12,14)}`;
}

export function validateCustomer(customer) {
  const errors = {};
  if (!customer.name || customer.name.trim().length < 3) errors.name = 'Nome é obrigatório (mínimo 3 caracteres)';
  if (!customer.document || onlyDigits(customer.document).length < 11) errors.document = 'Documento inválido (CPF/CNPJ)';
  if (!customer.phone || onlyDigits(customer.phone).length < 10) errors.phone = 'Telefone inválido';
  return errors;
}

export function validateAddress(addr) {
  const errors = {};
  if (!addr.street || addr.street.trim().length < 3) errors.street = 'Rua é obrigatória';
  if (!addr.number || addr.number.toString().trim().length === 0) errors.number = 'Número é obrigatório';
  if (!addr.city || addr.city.trim().length < 2) errors.city = 'Cidade é obrigatória';
  if (!addr.state || addr.state.trim().length < 2) errors.state = 'Estado inválido';
  if (!addr.zipCode || onlyDigits(addr.zipCode).length < 8) errors.zipCode = 'CEP inválido';
  return errors;
}
