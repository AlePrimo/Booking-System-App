import { useState } from "react";

export default function BookingModal({ service, onClose, onSubmit }) {
  const [dateTime, setDateTime] = useState("");
  const [error, setError] = useState("");

  const handleReserveClick = () => {
    if (!dateTime) {
      setError("Debes seleccionar fecha y hora");
      return;
    }
    const isoDate = new Date(dateTime);
    if (isNaN(isoDate.getTime())) {
      setError("Fecha inválida");
      return;
    }
    onSubmit({ bookingDateTime: isoDate.toISOString() });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded shadow w-96">
        <h2 className="text-xl font-bold mb-4">Reservar {service.name}</h2>

        <label className="block mb-2">
          Fecha y hora:
          <input
            type="datetime-local"
            value={dateTime}
            onChange={(e) => setDateTime(e.target.value)}
            className="w-full border px-2 py-1 rounded mt-1"
          />
        </label>

        {error && <p className="text-red-500 mb-2">{error}</p>}

        <div className="flex justify-end gap-2 mt-4">
          <button onClick={onClose} className="px-4 py-2 border rounded">
            Cancelar
          </button>
          <button
            onClick={handleReserveClick}
            className="px-4 py-2 bg-indigo-600 text-white rounded"
          >
            Reservar
          </button>
        </div>
      </div>
    </div>
  );
}

