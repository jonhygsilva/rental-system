import { useState, useRef } from "react";
import { createEquipment } from "./api/EquipmentsApi";
import { useNavigate } from "react-router-dom";
import Breadcrumbs from "../../shared/components/Breadcrumbs";
import Card from "../../components/Card";
import { extractApiError } from "../../utils/apiErrors";
import { motion } from "framer-motion";

export default function EquipmentForm() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    type: "",
    status: "DISPONIVEL",
    description: "",
    dailyRate: "",
    latitude: "",
    longitude: "",
    userId: 1,
  });
  const [errors, setErrors] = useState({});
  const nameRef = useRef(null);

  const validate = (f) => {
    const e = {};
    if (!f.name || f.name.trim().length < 2) e.name = "Nome é obrigatório (min 2 caracteres).";
    if (!f.type || f.type.trim().length === 0) e.type = "Tipo é obrigatório.";
    if (!f.dailyRate || Number(f.dailyRate) <= 0) e.dailyRate = "Informe um valor diário válido.";
    return e;
  };

  const handle = (e) => {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: undefined }));
  };

  const submit = (ev) => {
    ev.preventDefault();
    const e = validate(form);
    setErrors(e);
    if (Object.keys(e).length) {
      if (e.name && nameRef.current) nameRef.current.focus();
      return;
    }

    const payload = {
      ...form,
      latitude: form.latitude ? Number(form.latitude) : null,
      longitude: form.longitude ? Number(form.longitude) : null,
      dailyRate: Number(form.dailyRate),
    };

    // In absence of real file upload endpoint, send payload without file
    createEquipment(payload)
      .then(() => navigate("/equipments"))
      .catch((err) => {
        alert(extractApiError(err, 'Erro ao salvar o equipamento. Tente novamente.'));
      });
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.25 }} className="p-6">
      <Breadcrumbs items={[{ label: "Equipamentos", to: "/equipments" }, { label: "Novo Equipamento" }]} />

      <h1 className="text-2xl font-semibold mb-4">Novo Equipamento</h1>

      <form onSubmit={submit} className="space-y-4">
        <Card>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
            <div className="md:col-span-2">
              <label className="block text-xs text-gray-600 mb-1">Nome</label>
              <input ref={nameRef} name="name" value={form.name} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
              {errors.name && <p className="mt-1 text-xs text-red-600">{errors.name}</p>}

              <label className="block text-xs text-gray-600 mt-3 mb-1">Tipo</label>
              <input name="type" value={form.type} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
              {errors.type && <p className="mt-1 text-xs text-red-600">{errors.type}</p>}

              <label className="block text-xs text-gray-600 mt-3 mb-1">Descrição</label>
              <textarea name="description" value={form.description} onChange={handle} className="w-full border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" rows={4} />
            </div>

            <div className="flex flex-col gap-3">
              <label className="block text-xs text-gray-600 mb-1">Status</label>
              <select name="status" value={form.status} onChange={handle} className="border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400">
                <option value="DISPONIVEL">Disponível</option>
                <option value="EM_USO">Em Uso</option>
                <option value="MANUTENCAO">Manutenção</option>
              </select>

              <label className="block text-xs text-gray-600 mt-3 mb-1">Preço/Dia</label>
              <input name="dailyRate" type="number" value={form.dailyRate} onChange={handle} className="border border-gray-200 rounded px-3 py-2 text-sm focus:outline-none focus:border-gray-400" />
              {errors.dailyRate && <p className="mt-1 text-xs text-red-600">{errors.dailyRate}</p>}
            </div>
          </div>
        </Card>

        <div className="flex justify-end">
          <button type="submit" className="px-4 py-2 bg-gray-900 text-white rounded text-sm hover:bg-gray-800 transition">Salvar Equipamento</button>
        </div>
      </form>
    </motion.div>
  );
}
