import { useEffect, useState, useRef } from "react";
import { getCustomer, updateCustomer } from "./api/customersApi";
import { useNavigate, useParams } from "react-router-dom";
import Breadcrumbs from "../../shared/components/Breadcrumbs";
import Card from "../../components/Card";
import { motion } from "framer-motion";
import UnsavedChangesModal from "../../components/UnsavedChangesModal";
import {
  formatPhone,
  formatDocument,
  formatCEP,
  validateCustomer,
  validateAddress,
} from "../../utils/inputHelpers";

export default function CustomerEdit() {
  const { id } = useParams();
  const navigate = useNavigate();

  // Estado inicial
  const [customer, setCustomer] = useState({
    name: "",
    document: "",
    phone: "",
  });

  const [addresses, setAddresses] = useState([]);

  const [errors, setErrors] = useState({});
  const [addrErrors, setAddrErrors] = useState([]);
  const [apiError, setApiError] = useState(null);
  const nameRef = useRef(null);
  const documentRef = useRef(null);
  const phoneRef = useRef(null);
  const addressRefs = useRef([]);

  const focusFirstError = (localErrors = {}, localAddrErrors = []) => {
    if (localErrors.name && nameRef.current) {
      nameRef.current.focus();
      return;
    }
    if (localErrors.document && documentRef.current) {
      documentRef.current.focus();
      return;
    }
    if (localErrors.phone && phoneRef.current) {
      phoneRef.current.focus();
      return;
    }

    for (let i = 0; i < localAddrErrors.length; i++) {
      const ae = localAddrErrors[i] || {};
      const refs = addressRefs.current[i] || {};
      const order = ["street", "number", "neighborhood", "zipCode", "city", "state", "complement"];
      for (const key of order) {
        if (ae[key] && refs[key] && refs[key].focus) {
          refs[key].focus();
          return;
        }
      }
    }
  };

  // Para detectar alterações
  const originalData = useRef(null);
  const [hasChanges, setHasChanges] = useState(false);

  // Modal
  const [showUnsavedModal, setShowUnsavedModal] = useState(false);

  useEffect(() => {
    getCustomer(id).then((res) => {
      const data = res.data;

      const initialCustomer = {
        name: data.name,
        document: data.document,
        phone: data.phone,
      };

      const initialAddresses =
        data.addresses?.length
          ? data.addresses
          : [{ street: "", number: "", neighborhood: "", city: "", state: "", zipCode: "", complement: "" }];

      // salva versão original (para comparar)
      originalData.current = {
        customer: initialCustomer,
        addresses: JSON.stringify(initialAddresses),
      };

      setCustomer(initialCustomer);
      setAddresses(initialAddresses);
      setErrors(validateCustomer(initialCustomer));
      setAddrErrors(initialAddresses.map((a) => validateAddress(a)));
    });
  }, [id]);

  // ------------ 🔍 DETECTAR ALTERAÇÕES ------------
  useEffect(() => {
    if (!originalData.current) return;

    const changed =
      JSON.stringify(customer) !== JSON.stringify(originalData.current.customer) ||
      JSON.stringify(addresses) !== originalData.current.addresses;

    setHasChanges(changed);
  }, [customer, addresses]);

  // ------------ HANDLERS ------------
  const handleCustomer = (e) => {
    const { name, value } = e.target;
    let formatted = value;
    if (name === "phone") formatted = formatPhone(value);
    if (name === "document") formatted = formatDocument(value);

    setCustomer((prev) => {
      const updated = { ...prev, [name]: formatted };
      setErrors(validateCustomer(updated));
      return updated;
    });
  };

  const handleAddress = (index, field, value) => {
    let val = value;
    if (field === "zipCode") val = formatCEP(value);

    const updated = [...addresses];
    updated[index][field] = val;
    setAddresses(updated);
    setAddrErrors(updated.map((a) => validateAddress(a)));
  };

  const addAddress = () => {
    setAddresses([
      ...addresses,
      { street: "", number: "", neighborhood: "", city: "", state: "", zipCode: "", complement: "" },
    ]);
  };

  const removeAddress = (index) => {
    setAddresses(addresses.filter((_, i) => i !== index));
  };

  // ------------ SUBMIT ------------
  const submit = (e) => {
    e.preventDefault();

    setApiError(null);
    const customerErrors = validateCustomer(customer);
    const addressesErrors = addresses.map((a) => validateAddress(a));
    setErrors(customerErrors);
    setAddrErrors(addressesErrors);

    const hasCustomerErrors = Object.keys(customerErrors).length > 0;
    const hasAddrErrors = addressesErrors.some((a) => Object.keys(a).length > 0);

    if (hasCustomerErrors || hasAddrErrors) {
      focusFirstError(customerErrors, addressesErrors);
      return;
    }

    updateCustomer(id, {
      ...customer,
      addresses,
    })
      .then(() => navigate("/customers"))
      .catch((err) => {
        let friendly = "Erro ao atualizar o cliente. Tente novamente mais tarde.";
        let parsedCustomerErrors = {};
        let parsedAddrErrors = [];
          if (err?.response?.data) {
            const d = err.response.data;
            if (typeof d === "string") friendly = d;
            else if (d.message) friendly = d.message;

            if (d.errors && typeof d.errors === "object") {
              if (d.errors.customer) parsedCustomerErrors = d.errors.customer;
              else parsedCustomerErrors = { ...parsedCustomerErrors, ...d.errors };

              if (d.errors.addresses) parsedAddrErrors = d.errors.addresses;
              setErrors(parsedCustomerErrors);
              if (parsedAddrErrors.length) setAddrErrors(parsedAddrErrors);
            }

            if (d.fieldErrors && typeof d.fieldErrors === "object") {
              parsedCustomerErrors = { ...parsedCustomerErrors, ...d.fieldErrors };
              setErrors((prev) => ({ ...prev, ...d.fieldErrors }));
            }
          } else if (err?.message) {
            friendly = err.message;
          }

          setApiError(friendly);
          focusFirstError(
            Object.keys(parsedCustomerErrors).length ? parsedCustomerErrors : customerErrors,
            parsedAddrErrors.length ? parsedAddrErrors : addressesErrors
          );
      });
  };

  // ------------ VOLTAR ------------
  const handleBack = () => {
    if (hasChanges) {
      setShowUnsavedModal(true);
      return;
    }
    navigate("/customers");
  };

  return (
    <motion.div
      className="max-w-4xl mx-auto p-6 space-y-6"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.3 }}
    >
      <Breadcrumbs
        items={[
          { label: "Clientes", to: "/customers" },
          { label: customer.name || "Editar" },
        ]}
      />

      <div className="flex items-start justify-between gap-6">
        <div>
          <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900">Editar Cliente</h1>
          <p className="text-sm text-gray-500 mt-1">Atualize os dados do cliente e seus endereços.</p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handleBack}
            type="button"
            className="px-3 py-1.5 border border-gray-200 text-gray-700 rounded text-sm hover:bg-gray-50 transition"
          >
            Voltar
          </button>

          <button
            form="customer-form"
            type="submit"
            disabled={!hasChanges}
            className={`px-3 py-1.5 rounded text-sm transition
              ${hasChanges ? 'bg-gray-900 text-white hover:bg-gray-800' : 'bg-gray-200 cursor-not-allowed text-gray-400'}`}
          >
            Salvar
          </button>
        </div>
      </div>

      <form id="customer-form" onSubmit={submit} className="space-y-6">
        {apiError && (
          <div role="alert" className="mb-4 rounded border border-red-200 bg-red-50 p-3">
            <p className="text-sm text-red-700">{apiError}</p>
          </div>
        )}
        <motion.div
          initial={{ opacity: 0, y: 8 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25 }}
        >
          <Card title="Dados do Cliente">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">Nome</label>
                <input
                  ref={nameRef}
                  name="name"
                  placeholder="Nome completo"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm bg-white focus:outline-none focus:border-gray-400"
                  value={customer.name}
                  onChange={handleCustomer}
                />
                {errors.name && (
                  <p className="mt-1 text-sm text-red-600">{errors.name}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">Documento</label>
                <input
                  ref={documentRef}
                  name="document"
                  placeholder="CPF / CNPJ"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm bg-white focus:outline-none focus:border-gray-400"
                  value={customer.document}
                  onChange={handleCustomer}
                />
                {errors.document && (
                  <p className="mt-1 text-sm text-red-600">{errors.document}</p>
                )}
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-600 mb-1">Telefone</label>
                <input
                  ref={phoneRef}
                  name="phone"
                  placeholder="(00) 00000-0000"
                  className="w-full border border-gray-200 rounded px-3 py-2 text-sm bg-white focus:outline-none focus:border-gray-400"
                  value={customer.phone}
                  onChange={handleCustomer}
                />
                {errors.phone && (
                  <p className="mt-1 text-sm text-red-600">{errors.phone}</p>
                )}
              </div>
            </div>
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.05 }}
        >
          <Card title="Endereços">
            <div className="flex items-center justify-between mb-3">
              <p className="text-sm text-gray-500">Gerencie os endereços do cliente.</p>
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
                <div key={idx} className="border border-gray-200 rounded p-3">
                  <div className="grid grid-cols-1 md:grid-cols-4 gap-2">
                    <div className="md:col-span-2">
                      <label className="block text-xs text-gray-600 mb-1">Rua</label>
                      <input
                        placeholder="Rua"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], street: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.street}
                        onChange={(e) => handleAddress(idx, "street", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].street && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].street}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Número</label>
                      <input
                        placeholder="Nº"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], number: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.number}
                        onChange={(e) => handleAddress(idx, "number", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].number && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].number}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Bairro</label>
                      <input
                        placeholder="Bairro"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], neighborhood: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.neighborhood}
                        onChange={(e) => handleAddress(idx, "neighborhood", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].neighborhood && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].neighborhood}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">CEP</label>
                      <input
                        placeholder="CEP"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], zipCode: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.zipCode}
                        onChange={(e) => handleAddress(idx, "zipCode", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].zipCode && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].zipCode}</p>
                      )}
                    </div>

                    {/* Complemento is shown inline above; duplicate block removed for compactness */}

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Cidade</label>
                      <input
                        placeholder="Cidade"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], city: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.city}
                        onChange={(e) => handleAddress(idx, "city", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].city && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].city}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Estado</label>
                      <input
                        placeholder="UF"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], state: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.state}
                        onChange={(e) => handleAddress(idx, "state", e.target.value)}
                      />
                      {addrErrors[idx] && addrErrors[idx].state && (
                        <p className="mt-1 text-xs text-red-600">{addrErrors[idx].state}</p>
                      )}
                    </div>
                    <div>
                      <label className="block text-xs text-gray-600 mb-1">Complemento (opcional)</label>
                      <input
                        placeholder="Complemento"
                        ref={(el) => (addressRefs.current[idx] = { ...addressRefs.current[idx], complement: el })}
                        className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400"
                        value={addr.complement}
                        onChange={(e) => handleAddress(idx, "complement", e.target.value)}
                      />
                    </div>
                  </div>

                  <div className="flex justify-end mt-3">
                    {addresses.length > 1 && (
                      <button
                        type="button"
                        onClick={() => removeAddress(idx)}
                        className="px-3 py-1 border border-gray-300 text-gray-600 rounded text-sm hover:bg-gray-50 transition"
                      >
                        Remover
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </motion.div>
      </form>

      <UnsavedChangesModal
        show={showUnsavedModal}
        onCancel={() => setShowUnsavedModal(false)}
        onConfirm={() => navigate("/customers")}
      />
    </motion.div>
  );
}
