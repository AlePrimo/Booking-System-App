export default function ConfirmBookingModal({ service, bookingData, onConfirm, onCancel, submitting }) {
  if (!service || !bookingData) return null;

  const dateStr = new Date(bookingData.bookingDateTime).toLocaleString();

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-xl font-bold mb-4">Confirmar reserva</h2>
        <p>
          ¿Desea confirmar la reserva del servicio{" "}
          <strong>{service.name}</strong> el día{" "}
          <strong>{dateStr}</strong>?
        </p>

        <div className="flex justify-end gap-2 mt-6">
          <button
            onClick={onCancel}
            className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
            disabled={submitting}
          >
            Cancelar
          </button>
          <button
            onClick={onConfirm}
            disabled={submitting}
            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
          >
            {submitting ? "Reservando..." : "Confirmar"}
          </button>
        </div>
      </div>
    </div>
  );
}

