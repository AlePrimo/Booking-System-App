export default function ConfirmBookingModal({ service, bookingData, onConfirm, onCancel }) {
  if (!service || !bookingData) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-xl font-bold mb-4">Confirmar reserva</h2>
        <p>
          ¿Desea confirmar la reserva del servicio{" "}
          <strong>{service.name}</strong> el día{" "}
          <strong>{new Date(bookingData.bookingDateTime).toLocaleString()}</strong>?
        </p>

        <div className="flex justify-end gap-2 mt-6">
          <button
            onClick={onCancel}
            className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
          >
            Cancelar
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
          >
            Confirmar
          </button>
        </div>
      </div>
    </div>
  );
}
