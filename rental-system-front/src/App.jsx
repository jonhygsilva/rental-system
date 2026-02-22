import { BrowserRouter, Route, Routes } from "react-router-dom";

import CustomerList from "./pages/customers/CustomerList";
import CustomerForm from "./pages/customers/CustomerForm";
import CustomerEdit from "./pages/customers/CustomerEdit";
import CustomerView from "./pages/customers/CustomerView";

import AppLayout from "./components/AppLoyalty";
import ProtectedRoute from "./components/ProtectedRoute";
import { AuthProvider } from "./context/AuthContext";

import EquipmentList from "./pages/equipments/EquipmentList";
import EquipmentForm from "./pages/equipments/EquipmentForm";
import EquipmentEdit from "./pages/equipments/EquipmentEdit";
import RentalsList from "./pages/rentals/RentalsList";
import RentalForm from "./pages/rentals/RentalForm";
import RentalEdit from "./pages/rentals/RentalEdit";
import RentalView from "./pages/rentals/RentalView";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Auth routes (no layout) */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected routes (with layout) */}
          <Route
            path="/*"
            element={
              <ProtectedRoute>
                <AppLayout>
                  <Routes>
                    {/* CUSTOMERS */}
                    <Route path="/" element={<CustomerList />} />
                    <Route path="/customers" element={<CustomerList />} />
                    <Route path="/customers/new" element={<CustomerForm />} />
                    <Route path="/customers/:id/edit" element={<CustomerEdit />} />
                    <Route path="/customers/view/:id" element={<CustomerView />} />

                    {/* EQUIPMENTS */}
                    <Route path="/equipments" element={<EquipmentList />} />
                    <Route path="/equipments/new" element={<EquipmentForm />} />
                    <Route path="/equipments/:id" element={<EquipmentEdit />} />

                    {/* RENTALS */}
                    <Route path="/rentals" element={<RentalsList />} />
                    <Route path="/rentals/new" element={<RentalForm />} />
                    <Route path="/rentals/:id" element={<RentalView />} />
                    <Route path="/rentals/:id/edit" element={<RentalEdit />} />
                  </Routes>
                </AppLayout>
              </ProtectedRoute>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
