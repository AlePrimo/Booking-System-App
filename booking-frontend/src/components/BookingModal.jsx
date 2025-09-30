import { useState } from "react";
import { createBooking } from "../api/bookingService";

export default function BookingModal({ service, onClose }) {
  const [dateTime, setDateTime] = useState("");
  const [confirming, setConfirming] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleReserve = async () => {
    setLoading(true);
    setError("");
    try {
      const token = localStorage.getItem("accessToken");
      const customerId = localStorage.getItem("userId"); // asumiendo que lo guardás en login

      const booking = {
        customerId: parseInt(customerId),
        offeringId: service.id,
        bookingDateTime: dateTime,
        status: "PENDING",
      };

      await createBooking(booking, token);
      alert("Reserva creada con éxito ✅");
      onClose();
    } catch (err) {
      console.error(err);
      setError("No se pudo crear la reserva.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center">
      <div className="bg-white p-6 rounded shadow w-96">
        <h2 className="text-xl font-bold mb-4">
          Reservar {service.name}
        </h2>

        {!confirming ? (
          <>
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
              <button
                onClick={onClose}
                className="px-4 py-2 border rounded"
              >
                Cancelar
              </button>
              <button
                onClick={() => setConfirming(true)}
                className="px-4 py-2 bg-indigo-600 text-white rounded"
                disabled={!dateTime}
              >
                Reservar
              </button>
            </div>
          </>
        ) : (
          <>
            <p className="mb-4">
              ¿Desea confirmar la reserva para{" "}
              <strong>{service.name}</strong> el{" "}
              <strong>{dateTime}</strong>?
            </p>
            <div className="flex justify-end gap-2">
              <button
                onClick={() => setConfirming(false)}
                className="px-4 py-2 border rounded"
              >
                Volver
              </button>
              <button
                onClick={handleReserve}
                className="px-4 py-2 bg-green-600 text-white rounded"
                disabled={loading}
              >
                {loading ? "Guardando..." : "Confirmar"}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
