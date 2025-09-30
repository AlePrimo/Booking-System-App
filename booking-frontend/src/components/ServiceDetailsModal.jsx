export default function ServiceDetailsModal({ service, onClose, onReserve }) {
  if (!service) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-2xl font-bold mb-4">{service.name}</h2>
        <p className="mb-2">Precio: ${service.price}</p>
        <p className="mb-2">Proveedor: {service.providerName || service.provider?.name}</p>
        <p className="mb-4">{service.description || "Sin descripción"}</p>

        <div className="flex justify-end gap-2">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
          >
            Cerrar
          </button>
          <button
            onClick={() => onReserve(service)}
            className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
          >
            Reservar
          </button>
        </div>
      </div>
    </div>
  );
}
