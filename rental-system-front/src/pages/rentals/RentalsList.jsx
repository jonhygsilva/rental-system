import { useEffect, useState } from 'react';
import { getRentals, deleteRental } from './api/RentalsApi';
import RentalTable from '../../components/RentalTable';
import DeleteModal from '../../components/DeleteModal';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Plus } from 'lucide-react';

export default function RentalsList() {
  const [rentals, setRentals] = useState([]);
  const [deleteId, setDeleteId] = useState(null);

  const load = () => {
    getRentals().then((res) => setRentals(res.data || []));
  };

  useEffect(() => { load(); }, []);

  return (
    <motion.div className="p-6" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }}>
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Aluguéis</h1>
          <p className="text-gray-500">Crie, edite e visualize aluguéis.</p>
        </div>

        <div className="flex items-center gap-3">
          <Link to="/rentals/new" className="inline-flex items-center gap-2 px-3 py-1.5 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">
            <Plus className="w-4 h-4" /> Novo Aluguel
          </Link>
        </div>
      </div>

      <div className="mt-4">
        <RentalTable rentals={rentals} />
      </div>

      <DeleteModal
        show={deleteId !== null}
        title="Excluir Aluguel"
        onCancel={() => setDeleteId(null)}
        onConfirm={() => {
          deleteRental(deleteId).then(() => { setDeleteId(null); load(); });
        }}
      />
    </motion.div>
  );
}
