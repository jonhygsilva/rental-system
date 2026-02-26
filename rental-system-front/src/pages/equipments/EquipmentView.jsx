import { useEffect, useState } from "react";
import { getEquipment } from "./api/EquipmentsApi";
import { useNavigate, useParams } from "react-router-dom";
import Breadcrumbs from "../../shared/components/Breadcrumbs";
import Card from "../../components/Card";
import { motion } from "framer-motion";

export default function EquipmentView() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [equipment, setEquipment] = useState(null);

  useEffect(() => {
    getEquipment(id).then((res) => setEquipment(res.data)).catch(() => setEquipment(null));
  }, [id]);

  if (!equipment) {
    return (
      <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }} className="p-6">
        <Breadcrumbs items={[{ label: "Equipamentos", to: "/equipments" }, { label: "Visualizar" }]} />
        <div className="mt-6">Equipamento não encontrado.</div>
      </motion.div>
    );
  }

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }} className="p-6">
      <Breadcrumbs items={[{ label: "Equipamentos", to: "/equipments" }, { label: "Visualizar" }]} />

      <h1 className="text-2xl font-semibold mb-4">Visualizar Equipamento</h1>

      <Card>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
          <div className="md:col-span-2 space-y-3">
            <div>
              <label className="block text-xs text-gray-600 mb-1">Nome</label>
              <div className="text-sm text-gray-800">{equipment.name}</div>
            </div>

            <div>
              <label className="block text-xs text-gray-600 mt-3 mb-1">Tipo</label>
              <div className="text-sm text-gray-800">{equipment.type}</div>
            </div>

            <div>
              <label className="block text-xs text-gray-600 mt-3 mb-1">Descrição</label>
              <div className="text-sm text-gray-800 whitespace-pre-wrap">{equipment.description || '-'}</div>
            </div>
          </div>

          <div className="flex flex-col gap-3">
            <div>
              <label className="block text-xs text-gray-600 mb-1">Status</label>
              <div className="text-sm text-gray-800">{equipment.status}</div>
            </div>

            <div>
              <label className="block text-xs text-gray-600 mt-3 mb-1">Preço/Dia</label>
              <div className="text-sm text-gray-800">R$ {equipment.dailyRate}</div>
            </div>

            <div className="mt-3">
              <button onClick={() => navigate(`/equipments/${id}/edit`)} className="px-3 py-1 bg-gray-900 text-white rounded text-sm hover:bg-gray-800">Editar</button>
            </div>
          </div>
        </div>
      </Card>
    </motion.div>
  );
}
