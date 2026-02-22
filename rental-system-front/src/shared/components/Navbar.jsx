import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="bg-gray-900 text-white px-8 py-4 flex gap-6 shadow">
      <Link className="hover:text-blue-400" to="/">Dashboard</Link>
      <Link className="hover:text-blue-400" to="/customers">Customers</Link>
      <Link className="hover:text-blue-400" to="/equipments">Equipments</Link>
      <Link className="hover:text-blue-400" to="/rentals">Aluguéis</Link>
    </nav>
  );
}
