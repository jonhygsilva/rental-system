import { useEffect, useState } from "react";
import { getEquipments, deleteEquipment } from "./api/EquipmentsApi";
import DeleteModal from "../../components/DeleteModal";
import { Link } from "react-router-dom";
import EquipmentTable from "./components/EquipmentTable";
import { Plus } from "lucide-react";
import { motion } from "framer-motion";


export default function EquipmentList() {
  const [equipments, setEquipments] = useState([]);
  const [deleteId, setDeleteId] = useState(null);

  const load = () => {
    return getEquipments()
      .then((res) => setEquipments(res.data || []))
      .catch((err) => {
        console.error("Erro ao carregar equipamentos:", err);
        setEquipments([]);
      });
  };

  useEffect(() => {
    load();
  }, []);

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Equipamentos</h1>
          <p className="text-gray-500">Gerencie seus equipamentos — tipo, status, imagem e descrição.</p>
        </div>

        <Link to="/equipments/new" className="inline-flex items-center gap-2 px-3 py-1.5 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">
          <Plus className="w-4 h-4" />
          Novo Equipamento
        </Link>
      </div>

      <div className="mt-4">
        <EquipmentTable equipments={equipments} />
      </div>

      <DeleteModal
        show={deleteId !== null}
        title="Excluir Equipamento"
        onCancel={() => setDeleteId(null)}
        onConfirm={() => {
          deleteEquipment(deleteId).then(() => {
            setDeleteId(null);
            load();
          });
        }}
      />
    </motion.div>
  );
}
