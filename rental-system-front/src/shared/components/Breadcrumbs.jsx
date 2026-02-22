import { Link } from 'react-router-dom';
// eslint-disable-next-line no-unused-vars
import { motion } from 'framer-motion';
import { ChevronRight } from "lucide-react";

export default function Breadcrumbs({ items = [] }) {
  return (
    <motion.nav
      className="flex items-center text-sm text-gray-600 mb-6"
      initial={{ opacity: 0, y: -4 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.25 }}
    >
      {items.map((item, index) => {
        const isLast = index === items.length - 1;

        return (
          <div key={index} className="flex items-center">
            {/* LINK */}
            {item.to && !isLast ? (
              <Link
                to={item.to}
                className="hover:text-gray-900 transition font-medium"
              >
                {item.label}
              </Link>
            ) : (
              <span className="text-gray-400 font-medium">{item.label}</span>
            )}

            {/* ARROW */}
            {!isLast && (
              <ChevronRight className="w-4 h-4 mx-2 text-gray-400" />
            )}
          </div>
        );
      })}
    </motion.nav>
  );
}
