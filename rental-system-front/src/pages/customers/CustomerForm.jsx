
import { useState } from "react";
import { createCustomer } from "./api/CustomersApi";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Breadcrumbs from "../../shared/components/Breadcrumbs";
import Card from "../../components/Card";
import { extractApiError } from "../../utils/apiErrors";

export default function CustomerForm() {
  const navigate = useNavigate();

  const [customer, setCustomer] = useState({
    name: "",
    document: "",
    phone: "",
  });

  const [addresses, setAddresses] = useState([
    { street: "", number: "", neighborhood: "", city: "", state: "", zipCode: "", complement: "" }
  ]);

  const handleCustomer = (e) => {
    setCustomer({ ...customer, [e.target.name]: e.target.value });
  };

  const handleAddress = (index, field, value) => {
    const updated = [...addresses];
    updated[index][field] = value;
    setAddresses(updated);
  };

  const addAddress = () => {
    setAddresses([
      ...addresses,
      { street: "", number: "", neighborhood: "", city: "", state: "", zipCode: "", complement: "" }
    ]);
  };

  const removeAddress = (index) => {
    setAddresses(addresses.filter((_, i) => i !== index));
  };

  const submit = (e) => {
    e.preventDefault();
    
    // Validate customer fields
    if (!customer.name.trim()) {
      alert('Por favor, preencha o nome do cliente.');
      return;
    }
    
    if (!customer.document.trim()) {
      alert('Por favor, preencha o documento do cliente.');
      return;
    }
    
    if (!customer.phone.trim()) {
      alert('Por favor, preencha o telefone do cliente.');
      return;
    }
    
    // Validate at least one address exists
    if (addresses.length === 0) {
      alert('Por favor, adicione pelo menos um endereço.');
      return;
    }
    
    // Validate all addresses have required fields
    const incompleteAddresses = addresses.filter(addr => 
      !addr.street.trim() || !addr.number.trim() || !addr.neighborhood.trim() || 
      !addr.city.trim() || !addr.state.trim() || !addr.zipCode.trim()
    );
    
    if (incompleteAddresses.length > 0) {
      alert('Por favor, preencha todos os campos obrigatórios dos endereços.');
      return;
    }
    
    createCustomer({ ...customer, addresses })
      .then(() => navigate("/customers"))
      .catch((err) => {
        alert(extractApiError(err, 'Erro ao salvar o cliente. Tente novamente.'));
      });
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.3 }}
      className="p-8"
    >
      {/* --- Breadcrumbs --- */}
      <Breadcrumbs
        items={[
          { label: "Clientes", to: "/customers" },
          { label: "Novo Cliente" }
        ]}
      />

      {/* --- Título --- */}
      <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900 mb-6">Novo Cliente</h1>

      {/* --- Form --- */}
      <form onSubmit={submit} className="space-y-6">

        {/* --- CARD: Dados do Cliente --- */}
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25 }}
        >
          <Card title="Dados do Cliente">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm text-gray-600 mb-1">Nome <span className="text-gray-400 text-xs">*</span></label>
                <input
                  name="name"
                  placeholder="Nome completo"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                  onChange={handleCustomer}
                  required
                />
              </div>

              <div>
                <label className="block text-sm text-gray-600 mb-1">Documento <span className="text-gray-400 text-xs">*</span></label>
                <input
                  name="document"
                  placeholder="CPF / CNPJ"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                  onChange={handleCustomer}
                  required
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm text-gray-600 mb-1">Telefone <span className="text-gray-400 text-xs">*</span></label>
                <input
                  name="phone"
                  placeholder="(00) 00000-0000"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                  onChange={handleCustomer}
                  required
                />
              </div>
            </div>
          </Card>
        </motion.div>

        {/* --- CARD: Endereços --- */}
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.1 }}
        >
          <Card title="Endereços">
            <div className="flex items-center justify-between mb-3">
              <div>
                <p className="text-sm text-gray-500">Endereços do cliente</p>
                <p className="text-xs text-gray-500 mt-0.5">Mínimo de 1 endereço obrigatório • Complemento é opcional</p>
              </div>
              <button
                type="button"
                onClick={addAddress}
                className="px-3 py-1.5 border border-gray-300 text-gray-700 rounded text-sm hover:bg-gray-50 transition"
              >
                + Adicionar
              </button>
            </div>

            <div className="flex flex-col gap-4">
              {addresses.map((addr, idx) => (
                <motion.div
                  key={idx}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.2 }}
                  className="border border-gray-200 rounded p-4"
                >
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Rua <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="Rua"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.street}
                        onChange={(e) => handleAddress(idx, "street", e.target.value)}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Número <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="Número"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.number}
                        onChange={(e) => handleAddress(idx, "number", e.target.value)}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Bairro <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="Bairro"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.neighborhood}
                        onChange={(e) => handleAddress(idx, "neighborhood", e.target.value)}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Complemento</label>
                      <input
                        placeholder="Complemento (opcional)"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.complement}
                        onChange={(e) => handleAddress(idx, "complement", e.target.value)}
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Cidade <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="Cidade"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.city}
                        onChange={(e) => handleAddress(idx, "city", e.target.value)}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Estado <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="Estado"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.state}
                        onChange={(e) => handleAddress(idx, "state", e.target.value)}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">CEP <span className="text-gray-400 text-xs">*</span></label>
                      <input
                        placeholder="CEP"
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.zipCode}
                        onChange={(e) => handleAddress(idx, "zipCode", e.target.value)}
                        required
                      />
                    </div>
                  </div>

                  {addresses.length > 1 && (
                    <div className="flex justify-end mt-3 pt-3 border-t border-gray-200">
                      <button
                        type="button"
                        onClick={() => removeAddress(idx)}
                        className="px-3 py-1 border border-gray-300 text-gray-600 rounded text-sm hover:bg-gray-50 transition"
                      >
                        Remover
                      </button>
                    </div>
                  )}
                </motion.div>
              ))}
            </div>
          </Card>
        </motion.div>

        {/* --- Botão Salvar --- */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25 }}
          className="flex justify-end"
        >
          <button className="px-4 py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">
            Salvar Cliente
          </button>
        </motion.div>
      </form>
    </motion.div>
  );
}
