import { Link, useLocation } from "react-router-dom";
import { Home, Users, Boxes, MapPin } from "lucide-react";

const navItems = [
  { to: "/", label: "Dashboard", icon: Home },
  { to: "/customers", label: "Clientes", icon: Users },
  { to: "/equipments", label: "Equipamentos", icon: Boxes },
  { to: "/rentals", label: "Aluguéis", icon: MapPin },
];

export default function SideNavbar() {
  const location = useLocation();

  const isActive = (to) => {
    if (to === "/") return location.pathname === "/";
    return location.pathname.startsWith(to);
  };

  return (
    <aside className="hidden md:flex w-56 flex-col bg-gray-800 pt-4 pb-6 h-[calc(100vh-3.5rem)] sticky top-14">
      <nav className="flex flex-col gap-0.5 px-3">
        {navItems.map((item) => {
          const active = isActive(item.to);
          const Icon = item.icon;
          return (
            <Link
              key={item.to}
              to={item.to}
              className={`flex items-center gap-3 px-3 py-2 rounded text-sm transition ${
                active
                  ? "bg-white/10 text-white font-medium"
                  : "text-gray-400 hover:text-white hover:bg-white/5"
              }`}
            >
              <Icon size={18} strokeWidth={active ? 2 : 1.5} />
              <span>{item.label}</span>
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
