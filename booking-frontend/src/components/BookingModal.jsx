import { useState } from "react";

export default function BookingModal({ service, onClose, onSubmit }) {
  const [dateTime, setDateTime] = useState("");

  if (!service) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ bookingDateTime: dateTime });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-2xl font-bold mb-4">Reservar {service.name}</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1 font-medium">Fecha y hora</label>
            <input
              type="datetime-local"
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
              className="w-full border px-3 py-2 rounded"
              required
            />
          </div>

          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
            >
              Reservar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
