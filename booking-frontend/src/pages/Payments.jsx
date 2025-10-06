import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Payments = () => {
  const navigate = useNavigate();
  const [selectedOption, setSelectedOption] = useState("");

  const handleSelectChange = (e) => {
    setSelectedOption(e.target.value);
  };

  const handlePayment = () => {
    alert("Pago realizado (por ahora no conectado a backend)");
  };

  const handleBack = () => {
    navigate("/dashboard");
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center justify-center p-6">
      <div className="bg-white shadow-lg rounded-2xl p-8 w-full max-w-md text-center">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">Pagos</h1>

        <div className="mb-6">
          <label
            htmlFor="payment-option"
            className="block text-gray-700 font-medium mb-2"
          >
            Seleccioná una opción de pago
          </label>
          <select
            id="payment-option"
            value={selectedOption}
            onChange={handleSelectChange}
            className="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400 focus:outline-none"
          >
            <option value="">-- Seleccionar --</option>
            <option value="1">Opción 1</option>
            <option value="2">Opción 2</option>
            <option value="3">Opción 3</option>
          </select>
        </div>

        <div className="flex flex-col gap-4">
          <button
            onClick={handlePayment}
            className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition"
          >
            Realizar Pago
          </button>

          <button
            onClick={handleBack}
            className="bg-gray-300 text-gray-800 py-2 px-4 rounded-lg hover:bg-gray-400 transition"
          >
            Volver al Dashboard
          </button>
        </div>
      </div>
    </div>
  );
};

export default Payments;



