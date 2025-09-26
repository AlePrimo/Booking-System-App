import Login from "./Login";
import Register from "./Register";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { user } = useAuth();

  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-120px)] px-4">
      <h1 className="text-3xl font-bold text-indigo-600 text-center">
        Bienvenido a Booking System
      </h1>
      <p className="mt-4 text-gray-600 text-center">
        Gestiona usuarios, reservas, pagos y más.
      </p>

      {!user && (
        <div className="mt-8 flex flex-col md:flex-row justify-center gap-6 w-full max-w-4xl">
          <div className="w-full md:w-1/2">
            <Login />
          </div>
          <div className="w-full md:w-1/2">
            <Register />
          </div>
        </div>
      )}
    </div>
  );
}
