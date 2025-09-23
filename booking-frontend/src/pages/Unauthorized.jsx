import React from "react";
import { Link } from "react-router-dom";

export default function Unauthorized() {
  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      <div className="bg-white p-8 rounded shadow text-center">
        <h1 className="text-2xl font-bold mb-4">Acceso denegado</h1>
        <p className="mb-6">No tenés permisos para ver esta página.</p>
        <Link to="/" className="inline-block px-4 py-2 bg-indigo-600 text-white rounded">
          Volver al inicio
        </Link>
      </div>
    </div>
  );
}
