import { motion } from "framer-motion";

export default function PageHeader({ title, subtitle, action }) {
  return (
    <motion.div
      className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6"
      initial={{ opacity: 0, y: -6 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.25 }}
    >
      <div>
        <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900 tracking-tight">{title}</h1>
        {subtitle && <p className="text-sm text-gray-500 mt-1">{subtitle}</p>}
      </div>

      {action && (
        <div className="flex items-center gap-2">
          {action}
        </div>
      )}

    </motion.div>
  );
}
