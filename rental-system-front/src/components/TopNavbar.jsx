import { Link, useNavigate } from "react-router-dom";
import { LogOut } from "lucide-react";
import { useAuth } from "../context/AuthContext";

export default function TopNavbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header className="fixed top-0 left-0 right-0 h-14 flex-shrink-0 bg-gray-900 z-40">
      <div className="h-full w-full px-6 flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2.5">
          <div className="h-8 w-8 rounded bg-white/10 text-white flex items-center justify-center text-xs font-semibold">
            RS
          </div>
          <span className="text-sm font-semibold text-white">Rental System</span>
        </Link>

        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-400">{user?.name || user?.email}</span>
          <button
            onClick={handleLogout}
            className="flex items-center gap-1.5 text-sm text-gray-400 hover:text-white transition"
          >
            <LogOut size={16} />
            Sair
          </button>
        </div>
      </div>
    </header>
  );
}
