export default function Register() {
  return (
    <div className="container mx-auto px-6 py-12">
      <h2 className="text-3xl font-bold mb-4">Registro</h2>
      <form className="max-w-md mx-auto bg-white p-6 shadow-md rounded-lg space-y-4">
        <input
          type="text"
          placeholder="Nombre completo"
          className="w-full p-2 border rounded"
        />
        <input
          type="email"
          placeholder="Email"
          className="w-full p-2 border rounded"
        />
        <input
          type="password"
          placeholder="Password"
          className="w-full p-2 border rounded"
        />
        <button className="w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700">
          Registrarse
        </button>
      </form>
    </div>
  );
}
