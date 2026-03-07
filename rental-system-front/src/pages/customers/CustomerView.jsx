import { useEffect, useState } from "react";
import { getCustomer, deleteCustomer } from "./api/customersApi";
import { useNavigate, useParams } from "react-router-dom";
import { motion } from "framer-motion";
import Breadcrumbs from "../../shared/components/Breadcrumbs";
import PageHeader from "../../components/PageHeader";
import DeleteModal from "../../components/DeleteModal";
import { Edit, Trash2 } from "lucide-react";
import { extractApiError } from "../../utils/apiErrors";

export default function CustomerView() {
  const { id } = useParams();
  const navigate = useNavigate();


  const [customer, setCustomer] = useState({});
  const [addresses, setAddresses] = useState([]);
  const [deleteModal, setDeleteModal] = useState(false); 

  useEffect(() => {
    getCustomer(id).then((res) => {
      const data = res.data;
      setCustomer(data);
      setAddresses(data.addresses || []);
    });
  }, [id]);

  return (
    <motion.div
      className="max-w-4xl mx-auto p-6 space-y-6"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.3 }}
    >
      {/* 🔵 BREADCRUMB */}
      <Breadcrumbs
        items={[
          { label: "Clientes", to: "/customers" },
          { label: customer.name || "Detalhes" }
        ]}
      />

      {/* 🔵 HEADER */}
      <PageHeader
        title={customer.name || "Carregando..."}
        subtitle="Visualize os dados completos do cliente."
        action={
          <>
            <button
              onClick={() => navigate(`/customers/${id}/edit`)}
              className="inline-flex items-center gap-2 px-3 py-1.5 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition"
            >
              <Edit className="w-4 h-4" />
              Editar
            </button>

            <button
              onClick={() => setDeleteModal(true)}
              className="inline-flex items-center gap-2 px-3 py-1.5 border border-gray-300 text-gray-600 rounded text-sm hover:bg-gray-50 transition"
            >
              <Trash2 className="w-4 h-4" />
              Excluir
            </button>
          </>
        }
      />

      {/* 🔵 CARD – Dados (compact) */}
      <motion.div
        className="bg-white border border-gray-200 rounded p-4"
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.22 }}
      >
        <h2 className="text-lg font-semibold text-gray-700 mb-3">Dados do Cliente</h2>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <div className="flex flex-col">
            <span className="text-xs text-gray-500">Nome</span>
            <div className="mt-1 text-sm text-gray-800 font-medium">{customer.name || '-'}</div>
          </div>

          <div className="flex flex-col">
            <span className="text-xs text-gray-500">Documento</span>
            <div className="mt-1 text-sm text-gray-800 font-medium">{customer.document || '-'}</div>
          </div>

          <div className="flex flex-col">
            <span className="text-xs text-gray-500">Telefone</span>
            <div className="mt-1 text-sm text-gray-800 font-medium">{customer.phone || '-'}</div>
          </div>
        </div>
      </motion.div>

      {/* 🔵 CARD – Endereços (compact list) */}
      <motion.div
        className="bg-white border border-gray-200 rounded p-4"
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.25 }}
      >
        <h2 className="text-lg font-semibold text-gray-700 mb-3">Endereços</h2>

        {addresses.length === 0 && (
          <p className="text-gray-500 italic">Nenhum endereço cadastrado.</p>
        )}

        <div className="flex flex-col gap-3">
          {addresses.map((addr, idx) => (
            <div key={idx} className="border rounded-md p-3 bg-gray-50">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Rua</span>
                  <span className="text-sm text-gray-800 font-medium">{addr.street || '-'}</span>
                </div>

                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Número</span>
                  <span className="text-sm text-gray-800 font-medium">{addr.number || '-'}</span>
                </div>

                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Bairro</span>
                  <span className="text-sm text-gray-800 font-medium">{addr.neighborhood || '-'}</span>
                </div>

                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Complemento</span>
                  <span className="text-sm text-gray-800">{addr.complement || '-'}</span>
                </div>

                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Cidade</span>
                  <span className="text-sm text-gray-800">{addr.city || '-'}</span>
                </div>

                <div className="flex flex-col">
                  <span className="text-xs text-gray-500">Estado</span>
                  <span className="text-sm text-gray-800">{addr.state || '-'}</span>
                </div>

                <div className="flex flex-col sm:col-span-2">
                  <span className="text-xs text-gray-500">CEP</span>
                  <span className="text-sm text-gray-800">{addr.zipCode || '-'}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </motion.div>

      {/* 🔵 VOLTAR */}
      <div className="flex justify-end">
        <button
          onClick={() => navigate("/customers")}
          className="px-3 py-1 bg-white border border-gray-200 text-gray-700 rounded-md shadow-sm hover:shadow transition text-sm"
        >
          Voltar
        </button>
      </div>

      {/* 🔴 MODAL DE EXCLUSÃO */}
      <DeleteModal
        show={deleteModal}
        title="Excluir Cliente"
        message="Tem certeza que deseja excluir este cliente? Esta ação não pode ser desfeita."
        onCancel={() => setDeleteModal(false)}
        onConfirm={() => {
          deleteCustomer(id)
            .then(() => navigate("/customers"))
            .catch((err) => {
              alert(extractApiError(err, 'Erro ao excluir o cliente. Tente novamente.'));
            });
        }}
      />
    </motion.div>
  );
}
