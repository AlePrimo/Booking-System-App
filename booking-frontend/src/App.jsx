import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Bookings from "./pages/Bookings";
import Offerings from "./pages/Offerings";
import Users from "./pages/Users";
import Payments from "./pages/Payments";
import Notifications from "./pages/Notifications";
import Login from "./pages/Login";
import Register from "./pages/Register";

function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="container mx-auto py-6 px-4">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/bookings" element={<Bookings />} />
          <Route path="/offerings" element={<Offerings />} />
          <Route path="/users" element={<Users />} />
          <Route path="/payments" element={<Payments />} />
          <Route path="/notifications" element={<Notifications />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </main>

      <footer className="bg-gray-800 text-white py-6 text-center">
        <p>&copy; {new Date().getFullYear()} Booking System. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

export default App;

